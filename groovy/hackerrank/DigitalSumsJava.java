import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;
import static java.math.RoundingMode.HALF_UP;

class Options {
    boolean mockInput;
    boolean debug;

    Options() {}

    Options(boolean mockInput, boolean debug) {
        this.mockInput = mockInput;
        this.debug = debug;
    }
    public Options mockInput() { mockInput = true; return this; }
    public Options debug()     { debug = true;     return this; }

    public static Options options() { return new Options(); }
}

class DigitalSumsJava {
    public static final BigDecimal TWO = ONE.multiply(BigDecimal.valueOf(2));
    private final boolean DEBUG;
    private final boolean MOCKINPUT;
    private static int N = 0;
    private static int P = 0;

    public DigitalSumsJava(Options options) {
        DEBUG = options.debug;
        MOCKINPUT = options.mockInput;
    }

    private long digitalSum(String numberString) {
        int count = 0; long sum = 0;
        char[] chars = numberString.toCharArray();
        for (int i = 0; i <= P; i++) {
            char character = chars[i];
            if (character != '.') {
                sum += Character.getNumericValue(character);
            }
        }
        return sum;
    }

    BigDecimal squareRoot(int number) {
        BigDecimal numberBD = new BigDecimal(number, DECIMAL64);
        BigDecimal currTry   = BigDecimal.valueOf(sqrt(numberBD.doubleValue()));
        BigDecimal almostZero = null;
        BigDecimal maxDeviation = BigDecimal.valueOf(1, P);
        while (true) {
            almostZero = currTry.pow(2).subtract(numberBD);
            if (almostZero.abs().compareTo(maxDeviation) < 0) {
                break;
            }
            currTry = currTry.subtract(almostZero.divide(
                                        currTry.multiply(TWO), HALF_UP));
        }
        return currTry;
    }

    void debug(String msg) {
        if (DEBUG)
            System.out.println(format("D: %s", msg != null ? msg.toString() : msg));
    }

    private void solve() {
        if (MOCKINPUT) {
            N = 2;
            P = 100;
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                N = Integer.valueOf(br.readLine()); // How many natural numbers to process
                P = Integer.valueOf(br.readLine()); // How many digits to sum for each irrational square root
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long totalSum = 0;
        for (int natural = 1; natural <= N; natural++) {
            BigDecimal root = squareRoot(natural);
            double rootDouble = root.doubleValue();
            if (rootDouble == floor(rootDouble)) {
                continue;
            }
            totalSum += digitalSum(root.toString());

            /*
            System.out.println(digitalSum(root.toString()));
            System.out.println(root.toString());

            org.jscience.mathematics.number.Real numberReal =
                    org.jscience.mathematics.number.Real.valueOf(natural);
            numberReal.setExactPrecision((int) (P * 2.3));
            org.jscience.mathematics.number.Real rootReal = numberReal.sqrt();

            System.out.println(digitalSum(rootReal.toString()));
            System.out.println(rootReal.toString());
            */
        }
        System.out.println(totalSum);
    }


    public static void main(String[] args) {
        new DigitalSumsJava( new Options()
//                .mockInput().debug()
        ).solve();
    }
}

