import static java.lang.Math.floor
import static java.lang.Math.log10
import static java.lang.Math.sqrt
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.TEN
import static java.math.MathContext.DECIMAL64

def digitalSumOld(BigDecimal root) {
    (1..P).collect {
        def log10Root = (int) floor(log10(root.doubleValue()))
        if (log10Root >= 0) {
            def tenToLog = TEN.pow(log10Root)
            def digit = root.divide(tenToLog).doubleValue() as Integer
            root = root.subtract(tenToLog.multiply(digit))
            return digit as Integer
        }
        root = root.multiply(TEN)
        def digit = floor(root.doubleValue())
        root = root.subtract(ONE.multiply(digit))
        return digit as Integer
    }
    .sum()
}

def digitalSum(BigDecimal root) {
    def digits = root.toString()

    def count = 0; sum = 0
    digits.each { digit ->
        if (digit =~ /[0-9]/ && count < P) {
            count++; sum += digit.toInteger()
        }
    }
    sum
}

def squareRoot(number) {
    def estimate = sqrt(number)
    if (estimate * estimate == number) { return estimate }

    def numberBD = new BigDecimal(number, DECIMAL64)
    def rootBD   = new BigDecimal(estimate, DECIMAL64)

    def state = [min: null, max: null, number: numberBD, maxPrecision: P*2, last: null]

    return convergeOnSqrt(rootBD, state)
}

def convergeOnSqrt(currTry, state) {
    def rootSquared = currTry * currTry
    debug currTry
    if (state.min != null && state.max != null &&
            state.min.toString()[0..Math.min(P, state.min.toString().length()-1)] ==
            state.max.toString()[0..Math.min(P, state.max.toString().length()-1)]) {
        return currTry
    }

    if (rootSquared < state.number) {
        state.min = currTry
        if (state.max == null) {
            return convergeOnSqrt(currTry.plus(ONE.divide(TEN.pow(currTry.scale))), state)
        }
        return convergeOnSqrt(currTry.plus(state.max).divide(ONE.multiply(2.0)), state)
    } else {
        state.max = currTry
        if (state.min == null) {
            return convergeOnSqrt(currTry.subtract(ONE.divide(TEN.pow(currTry.scale))), state)
        }
        return convergeOnSqrt(currTry.plus(state.min).divide(ONE.multiply(2.0)), state)
    }
}

DEBUG = false
def debug(msg) {
    if (DEBUG) println "D: ${msg ?: msg.toString()}"
}

BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
N = br.readLine().toInteger() // How many natural numbers to process
P = br.readLine().toInteger() // How many digits to sum for each irrational square root

(1..N).each { double natural ->
    BigDecimal root = squareRoot(natural)
    if (root.doubleValue() == floor(root.doubleValue())) {
        return
    }
    println digitalSum(root)
}

println digitalSum(new BigDecimal("1.434"))