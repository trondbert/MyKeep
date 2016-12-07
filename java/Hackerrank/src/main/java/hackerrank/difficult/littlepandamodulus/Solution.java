package hackerrank.difficult.littlepandamodulus;

import static java.lang.Integer.parseInt;
import static java.lang.Math.pow;

import java.util.Scanner;

/**
 * @author trond.
 */
public class Solution {

    final int maxLog = 2;

    static int logA;

    static int a;

    static int b;

    static long x;

    static int level;

    public static void main(final String[] args) {

        final Scanner scanner = new Scanner(System.in);
        final int taskCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < taskCount; i++) {
            final String[] split = scanner.nextLine().split(" ");
            a = parseInt(split[0]);
            b = parseInt(split[1]);
            x = parseInt(split[2]);
            logA = (int) Math.log10(a);
            level = 0;
            System.out.println(new Solution().power(a, b, x));
        }
    }

    long power(final long a, final long b, final long x) {
        level++;
        if ((logA * b) > maxLog) {
            final long sqrtB = (long) Math.sqrt(b);
            long divisor = 2;
            if (b % divisor != 0) {
                divisor = 3;
                while (b % divisor != 0 && b <= sqrtB) {
                    divisor += 2;
                }
            }
            if (divisor > sqrtB) {
                final long half = power(a, b / 2, x);
                final long almostAll = (half * half) % x;
                //System.out.printf("%d %d %d %d\n", level--, a, b, x);
                return (almostAll * a) % x;
            }
            //System.out.printf("%d %d %d %d\n", level--, a, b, x);
            return power(power(a, b / divisor, x), divisor, x);
        }
        //System.out.printf("%d %d %d %d\n", level--, a, b, x);
        return (long) (pow(a, b) % x);
    }
}
