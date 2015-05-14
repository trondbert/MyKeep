import static java.lang.String.format

class BrokerBot {
    List<String> inputLines
    Double money
    Integer stocksAvailable, remainingDays

    String DBfilename = "./brokerDB.txt"
    boolean debug
    Map<String, Stock> stocks

    static class Stock { String name; Double boughtFor = 0; Integer ownCount = 0; List<Double> prices
        String toString() { name }
    }
    static class Transaction { Stock stock; Integer howMany; boolean buy; boolean sell
        String toString() { "${stock.name} ${buy ? "BUY" : sell ? "SELL" : ""} $howMany" }
    }
    static class StockAssessment { Stock stock; Double buyRating; Double sellRating
        String toString() {
            "${stock.name} have: ${stock.ownCount}, price: ${format("%.3f", stock.prices[0])}," +
                    " BUY ${format("%.3f", buyRating)} SELL ${format("%.3f", sellRating)}"

        }
    }

    BrokerBot setup(List<String> taskLines) {
        inputLines = taskLines
        def (MM, KK, DD) = inputLines.remove(0).split(" ").collect { Double.valueOf(it) }
        money = MM
        stocksAvailable = KK
        remainingDays = DD
        stocks = [:]

        if (new File(DBfilename).exists()) {
            BufferedReader brDB = new BufferedReader(new FileReader(DBfilename))
            String line = null
            while ((line = brDB.readLine()) != null) {
                def (name, boughtFor) = line.split(" ")
                stocks.put(name, new Stock(name: name, boughtFor: Double.valueOf(boughtFor)))
            }
        }
        return this
    }

    private List<String> getLines(BufferedReader taskInfoReader) {
        def line = null
        def result = []
        while ((line = taskInfoReader.readLine()) != null) {
            result.push(line)
        }
        result
    }

    BrokerBot init(String brokerInput) {
        def reader = (brokerInput != null && brokerInput.length() > 0) ?
                new BufferedReader(new FileReader(brokerInput)) :
                new BufferedReader(new InputStreamReader(System.in))
        setup(getLines(reader))
    }

    def debugMsg(message) {
        if (debug)
            println "D:$message"
    }

    List<String> trade() {
        ArrayList stockAssessments = readAndAssess()

        def canSell = []
        def wishToBuy = []
        stockAssessments.each {
            debugMsg it
            if (it.stock.ownCount > 0 &&
                    it.sellRating > 0 &&
                    (it.buyRating > 0 || it.sellRating > 200)) {

                canSell.push(it)
            }
            if (it.buyRating > 5 && it.buyRating > it.sellRating ) {
                wishToBuy.push(it)
            }
        }
        def transactions = []
        wishToBuy.sort { a, b -> a.buyRating - b.buyRating }
        def sumBuyRatings = wishToBuy.sum { it.buyRating }

        buy(wishToBuy, transactions)

        sell(canSell, wishToBuy, transactions)

        def output = []
        transactions.each { trans ->
            output.push(trans.toString())
        }
        updateDB()
        output
    }

    private void sell(ArrayList canSell, ArrayList wishToBuy, transactions) {
        def sumBuyRatings = wishToBuy.sum { it.buyRating }
        canSell.sort { a, b -> b.buyRating - a.buyRating }
        debugMsg "canSell"
        def buying = []; buying.addAll(wishToBuy)
        canSell.each { stockAss ->
            def wantToBuy = buying.find { it.buyRating > (stockAss.sellRating * 0.4) }
            debugMsg stockAss
            if (wantToBuy != null) {
                transactions.push(new Transaction(stock: stockAss.stock, sell: true, howMany: stockAss.stock.ownCount))
                buying.remove(wantToBuy)
            } else if (stockAss.sellRating > 80 || (stockAss.sellRating > 5 && sumBuyRatings > 30)) {
                transactions.push(new Transaction(stock: stockAss.stock, sell: true, howMany: stockAss.stock.ownCount))
            }
        }
    }

    private void buy(ArrayList wishToBuy, transactions) {
        def sumBuyRatings = wishToBuy.sum { it.buyRating }
        def portions = wishToBuy.collect { it.buyRating / sumBuyRatings }
        def moneyLeft = money
        wishToBuy.eachWithIndex { val, idx ->
            debugMsg "portion ${val.stock.name} ${portions[idx]}"
            def shares = (int) ((money * portions[idx]) / val.stock.prices[0])
            if (idx == wishToBuy.size() - 1) shares = (int) (moneyLeft / val.stock.prices[0])
            if (shares < 1) return;

            transactions.push(new Transaction(stock: val.stock, buy: true, howMany: shares))
            moneyLeft -= (shares * val.stock.prices[0])
            updateBuyingPrice(val.stock, val.stock.prices[0])
            if (stocks[val.stock.name] == null)
                stocks[val.stock.name] = val.stock
        }
    }

    private ArrayList readAndAssess() {
        def stockAssessments = []

        (1..stocksAvailable).collect { inputLines.remove(0).split(" ") }.each { stockInfo ->
            String stockName = stockInfo[0]
            Integer sharesOwned = Integer.valueOf(stockInfo[1])
            List<Double> prices = (6..2).collect { inputIndex -> Double.valueOf(stockInfo[inputIndex]) }

            Stock stock = getOrAddNewStock(stockName)
            stock.ownCount = sharesOwned
            stock.prices = stock.prices != null ? prices[0] + stock.prices : prices

            stockAssessments.push(considerStock(stock))
        }
        stockAssessments
    }

    private void updateDB() {
        if (stocks.size() > 0) {
            BufferedWriter bwDB = new BufferedWriter(new FileWriter("./brokerDB.txt"))
            stocks.each { key, val ->
                bwDB.writeLine("$key ${val.boughtFor}")
            }
            bwDB.close()
        }
    }

    private Stock getOrAddNewStock(String stockName) {
        if (stocks[stockName] == null) {
            stocks[stockName] = new Stock()
            stocks[stockName].name = stockName
        }
        stocks[stockName]
    }

    def considerStock(Stock st) {
        return new StockAssessment(stock: st, buyRating: rateForBuying(st), sellRating: rateForSelling(st))
    }

    Double rateForBuying(Stock stock) {
        def earliestDay = Math.min(10, stock.prices.size()) - 1
        def lastDays = stock.prices[1..earliestDay]
        Double average = lastDays.sum() / lastDays.size()
        return (average - stock.prices[0]) * 100.0 / average
    }

    Double rateForSelling(Stock stock) {
        Double diff = stock.prices[0] - stock.boughtFor
        if (stock.boughtFor.intValue() == 0)
            return -100000

        if (diff > 0 )
            return diff * 100.0 / stock.boughtFor
        return (diff * 100.0) / (Math.log(remainingDays) * stock.boughtFor)
    }

    def updateBuyingPrice(stock, sharesBought) {
        def totalPrice = (stock.prices[0] * sharesBought) + (stock.ownCount * stock.boughtFor)
        def ownCountNextRound = stock.ownCount + sharesBought
        stock.boughtFor = totalPrice / ownCountNextRound
    }
}
//new BrokerBot().setup("").trade()
def brokerBot = new BrokerBot(debug: true)
def transactions = brokerBot.init("./BrokerInput.txt").trade()

println transactions.size()
transactions.each { println it }

//println ([2, 5, 6].sort {a, b -> b - a })

