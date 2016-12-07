package hackerrank.moderate.majorityoftheballsb;

import static java.lang.Math.max;
import static java.lang.Math.random;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Solution {

    private static char x = 'x';

    private static char v = 'v';

    private static char y = 'y';

    private static char w = 'w';

    private static Integer[] exampleBalls = new Integer[] { 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0,
                                                            1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0,
                                                            1, 0, 1, 1
    };

    static Integer[] balls;

    public static void main(String[] args) {
        randomizeBalls((exampleBalls.length / 2) + 10);
        debug(arrayAsString(exampleBalls));
        runWorld();
        //nextQuestion(3, 0, 0, 0, 0, new String[] { "1 0 YES" });
        //solve();
    }

    private static void randomizeBalls(final int onesCount) {
        for (int i = 0; i < exampleBalls.length; i++) {
            exampleBalls[i] = 0;
        }
        for (int i = 0; i < onesCount; i++) {
            int oneIndex;
            do {
                oneIndex = (int) (random() * exampleBalls.length);
            } while (exampleBalls[oneIndex].equals(1));
            exampleBalls[oneIndex] = 1;
        }
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

    static String nextQuestion(int n, String[] queries) {
        balls = new Integer[n];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = null;
        }

        int queriedCount = deduce(queries, balls);
        debug(arrayAsString(balls));

        int equalsCount = countEquals(balls);
        int firstIndex = firstIndex(balls);
        if (firstIndex >= 0) {
            int firstCount = getBall(firstIndex);

            if (firstCount > (n - queriedCount)) {
                return "" + firstIndex;
            }

            int secondIndex = secondIndex(balls);
            if (secondIndex != -1 && equalsCount > (n - queriedCount))
                return firstIndex(balls) + " " + secondIndex;

            int equalCountA = -1, equalCountB;
            for (equalCountB = 0; equalCountB < balls.length; equalCountB++) {
                if (getBall(equalCountB) > 0) {
                    if (equalCountA >= 0 && getBall(equalCountB) == getBall(equalCountA))
                        break;
                    equalCountA = equalCountB;
                }
            }
            if (equalCountA >= 0 && equalCountB < balls.length) {
                return equalCountA + " " + equalCountB;
            }
        }
        return getTwoUnqueried();
    }

    private static String getTwoUnqueried() {
        int i;
        do {
            i = (int) (random() * balls.length);
        } while (balls[i] != null);
        int j;
        do {
            j = (int) (random() * balls.length);
        } while (balls[j] != null || j == i);

        return i + " " + j;
    }

    private static int getBall(final Integer ball) {
        return balls[ball] != null? balls[ball] : 0;
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

    private static int deduce(final String[] query, final Integer[] balls) {
        Set<Integer> queried = new TreeSet<>();

        for (String ints : query) {
            String[] split = ints.split(" ");
            int i = Integer.parseInt(split[0]);
            int j = Integer.parseInt(split[1]);
            queried.add(i);
            queried.add(j);
            boolean equal = split[2].equals("YES");

            if (equal) {
                balls[i] = max(getBall(i) + getBall(j), 2);
                balls[j] = 0;
            }
            else {
                balls[i] = getBall(i) - getBall(j);
                balls[j] = 0;
            }
        }

        return queried.size();
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

    private static String arrayAsString(final Integer[] deduced) {
        StringBuilder sb = new StringBuilder();
        for (Integer c : deduced) {
            sb.append(c != null? c : "-").append(" ");
        }
        return sb.toString().trim();
    }

    private static void debug(Object o) {
        System.out.println(o);
    }

    private static boolean relative(final char ball) {
        return ball == v || ball == w;
    }

    private static boolean determined(final char[] deduced, final int i) {
        return deduced[i] == x || deduced[i] == y;
    }

    private static int countEquals(Integer[] balls) {
        int count = 0;
        for (int i = 0; i < balls.length; i++) {
            count += getBall(i);
        }
        return count;
    }

    private static int firstIndex(Integer[] balls) {
        int index = 0;
        for (Integer ball : balls) {
            if (ball != null && ball > 0)
                return index;
            index++;
        }
        return -1;
    }

    private static int secondIndex(Integer[] balls) {
        boolean foundFirst = false;
        int index = 0;
        for (int i = 0; i < balls.length; i++) {
            if (getBall(i) > 0) {
                if (foundFirst) {
                    return index;
                }
                foundFirst = true;
            }
            index++;
        }
        return -1;
    }

    private static void runWorld() {
        String[] queries = new String[0];
        for (final int exampleBall : exampleBalls) {
            String query = nextQuestion(exampleBalls.length, queries);
            debug(query);
            String[] split = query.split(" ");
            if (split.length < 2) {
                System.out.println("---" + query + "---");
                return;
            }
            int i = Integer.parseInt(split[0]);
            int j = Integer.parseInt(split[1]);

            String answer = exampleBalls[i].equals(exampleBalls[j])? "YES" : "NO";
            queries = Arrays.copyOf(queries, queries.length + 1);
            queries[queries.length - 1] = i + " " + j + " " + answer;
        }
    }
}
