package trond.java;

import static java.lang.Math.pow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author trond.
 */
public class MagiskKvadratRettFram {

    final static int numberBase = 5;

    int[][] b;

    int[] masks = new int[] { 15 * (int) pow(16, 3), 15 * (int) pow(16, 2), 15 * (int) pow(16, 1), 15 * (int) pow(16, 0), 1
    };

    public static void main(String[] args) {
        //System.out.println(new MagiskKvadratRettFram().pos(23453, 5));
        //System.out.println(new MagiskKvadratRettFram().pos(23453, 6));
        //System.out.println(new MagiskKvadratRettFram().inc(0x3333));
        new MagiskKvadratRettFram().solve();
    }

    private void solve() {
        HashMap<Integer, Integer> sums = new HashMap<>();

        b = new int[4][4];
        long countOfMatches = 0;

        double maxDoubleRow = numberBase * pow(16, 3);
        int sum1;
        int sum2;
        int sum3;
        for (int n1 = 0; n1 < maxDoubleRow; n1 = inc(n1)) {
            sum1 = pos(n1, 0) + pos(n1, 1) + pos(n1, 2) + pos(n1, 3);
            for (int n2 = 0; n2 < maxDoubleRow; n2 = inc(n2)) {
                sum2 = pos(n2, 0) + pos(n2, 1) + pos(n2, 2) + pos(n2, 3);
                if (sum2 != sum1)
                    continue;
                for (int n3 = 0; n3 < maxDoubleRow; n3 = inc(n3)) {
                    sum3 = pos(n3, 0) + pos(n3, 1) + pos(n3, 2) + pos(n3, 3);
                    if (sum3 != sum1)
                        continue;
                    for (int n4 = 0; n4 < maxDoubleRow; n4 = inc(n4)) {
                        int sum4 = pos(n4, 0) + pos(n4, 1) + pos(n4, 2) + pos(n4, 3);
                        if (sum4 != sum1)
                            continue;

                        //Vertical
                        int sum = pos(n1, 0) + pos(n2, 0) + pos(n3, 0) + pos(n4, 0);
                        if (sum != sum1)
                            continue;
                        sum = pos(n1, 1) + pos(n2, 1) + pos(n3, 1) + pos(n4, 1);
                        if (sum != sum1)
                            continue;
                        sum = pos(n1, 2) + pos(n2, 2) + pos(n3, 2) + pos(n4, 2);
                        if (sum != sum1)
                            continue;
                        sum = pos(n1, 3) + pos(n2, 3) + pos(n3, 3) + pos(n4, 3);
                        if (sum != sum1)
                            continue;
                        //Diagonal
                        sum = pos(n1, 0) + pos(n2, 1) + pos(n3, 2) + pos(n4, 3);
                        if (sum != sum1)
                            continue;
                        sum = pos(n1, 3) + pos(n2, 2) + pos(n3, 1) + pos(n4, 0);
                        if (sum != sum1)
                            continue;
                        countOfMatches++;
                        addToSums(sums, sum);
                        //printBoard(n1, n2, n3, n4);
                    }
                }
            }
        }
        System.out.println("Matches: " + countOfMatches);
        for (Integer sum : sums.keySet()) {
            System.out.println(sum + ": " + sums.get(sum));
        }
    }

    private int pos(int n, int p) {
        int result = (n & masks[p]) / masks[p + 1];
        return result;
    }

    int inc(int n) {
        int newN = n + 1;
        for (int i = 3; i > 0; i--) {
            int pos = pos(newN, i);
            if (pos >= numberBase) {
                newN = newN & ~masks[i];
                newN = (int) (newN + Math.pow(16, (4 - i)));
            }
        }
        return newN;
    }

    void addToSums(Map<Integer, Integer> sums, int sum) {
        Integer sumInMap = sums.get(sum);
        if (sumInMap == null)
            sumInMap = 0;
        sums.put(sum, sumInMap + 1);
    }

    void printBoard(int n1, int n2, int n3, int n4) {
        System.out.print("|");
        System.out.println("" + pos(n1, 0) + pos(n1, 1) + pos(n1, 2) + pos(n1, 3) +
                           pos(n2, 0) + pos(n2, 1) + pos(n2, 2) + pos(n2, 3) +
                           pos(n3, 0) + pos(n3, 1) + pos(n3, 2) + pos(n3, 3) +
                           pos(n4, 0) + pos(n4, 1) + pos(n4, 2) + pos(n4, 3));
    }
}
