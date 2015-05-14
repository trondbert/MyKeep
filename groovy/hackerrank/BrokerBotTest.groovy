/**
 * Created by trond on 27/04/15.
 */
class BrokerBotTester {

    static class State { Map<String, Integer> stocksOwned; Double money }

    def state = new State()

    void testIt(String filename) {
        state.stocksOwned = new HashMap<>()
        state.money = 100.0
        def todayPrices = [:]

        def br = new BufferedReader(new FileReader(filename))
        def inputLines
        while ( (inputLines = getInputForTheDay(br)) != null) {
            def roundNr = inputLines[0].split(" ")[2]
            inputLines.tail().each {
                def stockInfo = it.split(" ")
                def stockName = stockInfo[0]
                todayPrices[stockName] = stockInfo[6].toDouble()
                state.stocksOwned[stockName] = state.stocksOwned[stockName] ?: 0
            }

            def transactions = new BrokerBot(debug: true).setup(inputLines).trade()
            println transactions.size()
            transactions.each { println it }
            runTransactions(transactions, todayPrices)
            println "Money after round ${roundNr}: ${state.money}"
        }
    }

    private ArrayList<String> getInputForTheDay(BufferedReader br) {
        def dayHeader = br.readLine()?.split(" ")
        if (dayHeader == null) return null

        dayHeader[0] = state.money.toString()
        def lines = [dayHeader.join(" ")]

        def stockTypes = dayHeader[1].toInteger()
        (1..stockTypes).each {
            def line = br.readLine().split(" ")
            def stockName = line[0]
            def ownCount = state.stocksOwned[stockName] ?: 0
            lines.push( ([stockName, ownCount] + line[2..line.size()-1]).join(" "))
        }
        return lines
    }

    private Iterable<String> runTransactions(List<String> transactions, todayPrices) {
        transactions.each { trans ->
            def stockName = trans.split(" ")[0]
            def buy = trans.split(" ")[1] == "BUY"
            def sell = trans.split(" ")[1] == "SELL"
            def tradingCount = trans.split(" ")[2].toInteger()

            if (buy) {
                state.money -= todayPrices[stockName] * tradingCount
                state.stocksOwned[stockName] = state.stocksOwned[stockName] ?: 0
                state.stocksOwned[stockName] += tradingCount
            }
            if (sell) {
                state.money += todayPrices[stockName] * tradingCount
                state.stocksOwned[stockName] = state.stocksOwned[stockName] ?: 0
                state.stocksOwned[stockName] -= tradingCount
            }
        }
    }
}

new File("./brokerDB.txt").delete()
new BrokerBotTester().testIt("./StockPredictionInput.txt")

def arr = [1, 2, 3, 4]
println( [7, 8] + arr[2..3])
