import org.jscience.mathematics.number.Real

import javax.security.sasl.RealmCallback
import java.math.RoundingMode

import static Globals.TWO
import static java.lang.Math.floor
import static java.lang.Math.log10
import static java.lang.Math.sqrt
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.TEN
import static java.math.MathContext.DECIMAL64
import static java.math.RoundingMode.HALF_UP

class Globals {
    public static final BigDecimal TWO = ONE.multiply(2.0)
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

    def state = [number: numberBD, maxDeviation: BigDecimal.valueOf(1, P), last: null]

    return convergeOnSqrt(rootBD, state)
}

def convergeOnSqrt(BigDecimal currTry, state) {
    BigDecimal almostZero = currTry.pow(2).subtract(state.number)
    debug currTry
    debug almostZero
    if (almostZero.abs().compareTo(state.maxDeviation) < 0) {
        return currTry
    }

    BigDecimal derivative = currTry.multiply(TWO)
    debug derivative
    BigDecimal nextTry = currTry.subtract( almostZero.divide(derivative, HALF_UP) )

    state.last = currTry
    convergeOnSqrt(nextTry, state)
}

DEBUG = false

def debug(msg) {
    if (DEBUG) println "D: ${msg ?: msg.toString()}"
}

private void solve(options) {
    DEBUG = options.debug
    if (options.mockInput) {
        N = 2
        P = 100
    } else {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        N = br.readLine().toInteger() // How many natural numbers to process
        P = br.readLine().toInteger() // How many digits to sum for each irrational square root
    }

    (1..N).each { double natural ->
        BigDecimal root = squareRoot(natural)
        if (root.doubleValue() == floor(root.doubleValue())) {
            return
        }
        println digitalSum(root)
    }
}

solve( [ mockInput: true, debug: false ] )

println Real.valueOf(2.0).sqrt()