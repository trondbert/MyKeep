import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;
import static java.math.RoundingMode.HALF_DOWN;

class DigitalSumsJava {
    public static final BigDecimal TWO = ONE.multiply(BigDecimal.valueOf(2));
    private static int N = 0;
    private static int P = 0;

    private static BigDecimal DIGITS;
    private static BigDecimal PRECISION;
    private static int TEMP_SCALE;

    private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx, 2*P, RoundingMode.HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1){
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }

    public static void main(String[] args) throws IOException {
        long totalSum = 0;
        long singleSum = 0;
        BigDecimal numberBD;
        BigDecimal currTry;
        BigDecimal almostZero;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.valueOf(br.readLine()); // How many natural numbers to process
        P = Integer.valueOf(br.readLine()); // How many digits to sum for each irrational square root

        DIGITS = new BigDecimal(P);
        PRECISION = new BigDecimal(10).pow(P);
        TEMP_SCALE = (int)(1.8*P);

        BigDecimal maxDeviation = BigDecimal.valueOf(1, P);

        for (int natural = 1; natural <= N; natural++) {
            numberBD = new BigDecimal(natural, DECIMAL64);
            currTry = BigDecimal.valueOf(sqrt(numberBD.doubleValue()));
            currTry.setScale(TEMP_SCALE);
            while (true) {
                almostZero = currTry.pow(2).subtract(numberBD);
                if (almostZero.abs().compareTo(maxDeviation) < 0) {
                    break;
                }
                currTry = currTry.subtract(almostZero.divide(currTry.multiply(TWO), TEMP_SCALE, HALF_DOWN));
            }
            if (currTry.doubleValue() == floor(currTry.doubleValue())) {
                continue;
            }

            singleSum = 0;
            char[] chars = currTry.toString().toCharArray();
            for (int i = 0; i <= P; i++) {
                char character = chars[i];
                if (character != '.') {
                    singleSum += Character.getNumericValue(character);
                }
            }
            totalSum += singleSum;
        }
        System.out.println(totalSum);
    }
}

