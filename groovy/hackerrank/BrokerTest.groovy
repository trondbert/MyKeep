import static java.lang.Math.pow

def stock = new BrokerBot.Stock(name: "", boughtFor: null, ownCount: 0, prices: [1, 2, 3, 4, 5])
Double rate = new BrokerBot().rateForBuying(stock)
assertThat(rate).isEqualToFloat(71.43)

stock = new BrokerBot.Stock(name: "", boughtFor: null, ownCount: 0, prices: [4, -1, 0, 1, 8])
rate = new BrokerBot().rateForBuying(stock)
assertThat(rate).isEqualToFloat(-100)

stock = new BrokerBot.Stock(name: "", boughtFor: null, ownCount: 0, prices: [2, 4, 4, 4, 4])
rate = new BrokerBot().rateForBuying(stock)
assertThat(rate).isEqualToFloat(50)

def bot = new BrokerBot(remainingDays: 10)
stock = new BrokerBot.Stock(name: "", boughtFor: 10, ownCount: 0, prices: [20])
rate = bot.rateForSelling(stock)
assertThat(rate).isEqualToFloat(100)

stock = new BrokerBot.Stock(name: "", boughtFor: 10, ownCount: 0, prices: [5])
rate = bot.rateForSelling(stock)
assertThat(rate).isEqualToFloat(-21.71)

def assertThat(actual) {
    new Assertomaton(actual)
}

class Assertomaton {
    def actual

    Assertomaton(actual) {
        this.actual = actual
    }

    def isEqualToFloat(expected) {isEqualToFloat(expected, 2)}

    def isEqualToFloat(expected, accuracy) {
        def multiplier = pow(10.0, accuracy)
        if (Math.round(this.actual * multiplier) != Math.round(expected * multiplier))
            assert this.actual == expected
    }
}
