package hackerrank.moderate.majorityoftheballs;

import static java.lang.Math.max;

import java.util.Arrays;
import java.util.Scanner;

class Solution {

    private static char x = 'x';

    private static char v = 'v';

    private static char y = 'y';

    private static char w = 'w';

    private static int[] exampleBalls = new int[] { 0, 1, 1, 1, 0, 0, 1, 1 };

    public static void main(String[] args) {
        runWorld();
        //nextQuestion(3, 0, 0, 0, 0, new String[] { "1 0 YES" });
        //solve();
    }

    private static void solve() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextInt();
        in.nextInt();
        in.nextInt();
        in.nextInt();

        int query_size = in.nextInt();
        String[] query = new String[query_size];
        int x, y;
        String answer;

        for (int i = 0; i < query_size; i++) {
            x = in.nextInt();
            y = in.nextInt();
            answer = in.next();
            query[i] = x + " " + y + " " + answer;
        }
        System.out.println(nextQuestion(n, query));
    }

    static String nextQuestion(int n, String[] query) {
        final char[] deduced = new char[n];
        for (int i = 0; i < deduced.length; i++) {
            deduced[i] = '?';
        }
        int maxQueried = deduce(query, deduced);

        int firstEqual = firstEqual(deduced);
        int lastEqual = lastEqual(deduced);

        final int unequal = countUnequal(deduced);
        for (char color : new char[] { x, y }) {
            int count = count(deduced, color);
            if (count > (n - unequal) / 2) {
                return String.valueOf(first(deduced, color));
            }
        }

        if (firstEqual >= 0 && lastEqual > firstEqual && !determined(deduced, lastEqual)) {
            return firstEqual + " " + lastEqual;
        }
        debug(arrayAsString(deduced));

        return (maxQueried + 1) + " " + (maxQueried + 2);
    }

    private static int countUnequal(final char[] balls) {
        int unequal = 0;
        for (int i = 0; i < balls.length; i += 2) {
            if (balls[i] != balls[i + 1]) {
                unequal += 2;
            }
        }
        return unequal;
    }

    private static int deduce(final String[] query, final char[] deduced) {
        int maxQueried = -1;
        boolean foundEqual = false;

        for (String ints : query) {
            String[] split = ints.split(" ");
            int i = Integer.parseInt(split[0]);
            int j = Integer.parseInt(split[1]);
            boolean equal = split[2].equals("YES");
            char ballI = deduced[i];
            char ballJ = deduced[j];
            maxQueried = max(max(maxQueried, i), j);

            if (equal && !foundEqual) {
                foundEqual = true;
                deduced[i] = deduced[j] = x;
            }
            if (equal && !determined(deduced, j))
                deduced[j] = determined(deduced, i)? deduced[i] : v;
            if (equal && !determined(deduced, i))
                deduced[i] = determined(deduced, j)? deduced[j] : v;

            if (!equal && !determined(deduced, j))
                deduced[j] = determined(deduced, i)? (deduced[i] == x? y : x) : w;

            if (!equal && !determined(deduced, i))
                deduced[i] = determined(deduced, j)? (deduced[j] == x? y : x) : v;

            if (i % 2 == 0 && relative(ballI) && determined(deduced, i))
                deduced[i + 1] = (ballI == deduced[i + 1])? deduced[i] : (deduced[i] == x? y : x);

            if (j % 2 == 0 && relative(ballJ) && determined(deduced, j))
                deduced[j + 1] = (ballJ == deduced[j + 1])? deduced[j] : (deduced[j] == x? y : x);
        }

        return maxQueried;
    }

    private static int firstEqual(final char[] deduced) {
        for (int i = 0; i < deduced.length; i += 2) {
            if ((deduced[i] == v && deduced[i + 1] == v) || (deduced[i] == x && deduced[i + 1] == x)) {
                return i;
            }
        }
        return -1;
    }

    private static int lastEqual(final char[] deduced) {
        int lastEqual = -1;
        for (int i = 0; i < deduced.length; i += 2) {
            if ((deduced[i] == v && deduced[i + 1] == v) || (deduced[i] == x && deduced[i + 1] == x)) {
                lastEqual = i;
            }
        }
        return lastEqual;
    }

    private static String arrayAsString(final char[] deduced) {
        StringBuilder sb = new StringBuilder();
        for (char c : deduced) {
            sb.append(c).append(" ");
        }
        return sb.toString().trim();
    }

    private static void debug(Object o) {
        //System.out.println(o);
    }

    private static boolean relative(final char ball) {
        return ball == v || ball == w;
    }

    private static boolean determined(final char[] deduced, final int i) {
        return deduced[i] == x || deduced[i] == y;
    }

    private static int count(char[] balls, char color) {
        int count = 0;
        for (char ball : balls) {
            if (ball == color)
                count++;
        }
        return count;
    }

    private static int first(char[] balls, char color) {
        int index = 0;
        for (char ball : balls) {
            if (ball == color)
                return index;
            index++;
        }
        return -1;
    }

    private static void runWorld() {
        String[] queries = new String[0];
        for (final int exampleBall : exampleBalls) {
            String query = nextQuestion(exampleBalls.length, queries);
            String[] split = query.split(" ");
            int i = Integer.parseInt(split[0]);
            int j = Integer.parseInt(split[1]);

            String answer = exampleBalls[i] == exampleBalls[j]? "YES" : "NO";
            queries = Arrays.copyOf(queries, queries.length + 1);
            queries[queries.length - 1] = i + " " + j + " " + answer;
        }
    }
}
