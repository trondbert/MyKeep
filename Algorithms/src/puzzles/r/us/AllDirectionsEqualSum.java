package puzzles.r.us;

import javafx.util.Pair;

import java.util.*;

public class AllDirectionsEqualSum {

    static final int width = 4;
    static final int[][] boardArray = new int[width][width];
    AllDirectionsEqualBoard board = new AllDirectionsEqualBoard();

    private static final int hvorStortDatasett = 45;
    static Long[][] factorials = new Long[hvorStortDatasett][hvorStortDatasett];

    static Map<Integer, List<Integer>> factorialSegments = new HashMap();

    public static void main(String[] args) {
        //new AllDirectionsEqualSum().solve();

        factorials[1][0] = 1L;
        factorials[1][1] = 1L;

        for (int i = 1; i < hvorStortDatasett; i++) {
            factorials[i][i] = (long)i;
        }

        for (int all = 2; all < hvorStortDatasett; all++) {
            StringBuilder sb = new StringBuilder();
            for (int selection = 1; selection <= (all+1)/2; selection++) {
                long binomial;
                binomial = Math.max(0, fac(all, (all - selection + 1)) / fac(selection, 1));
                sb.append(binomial);
                sb.append(" ");
            }
            for (int rest = (all+1)/2 + 1; rest <= 100; rest++) {
                sb.append("0 ");
            }
            System.out.println(sb.toString());
        }
    }

    private static Long fac(int max, int min) {
        if (factorials[max][min] != null) {
            return factorials[max][min];
        }
        for (int i = min+1; i <= max; i++) {
            factorials[i][min] = i * factorials[i-1][min];
        }
        System.out.println(String.format("Fac [%d][%d]: %d", max, min, factorials[max][min]));
        return factorials[max][min];
    }

    private void solve() {
        long c = allCombinations(0,2,6);
        /*
               0 0-4, 1 0-3, 2 0-2, 3 0-1, 4 0,
          035: 0 5 10 15 20, 3 8 13 18, 6 11 16, 9 14, 12
          089: 0 9 18 27 36, 8 17 26 35, 16 25 34, 24 33, 32

          028: 0 8 16 24 32, 2 10 18 26, 4 12 20, 6 14, 8 : 14
          026: 0 6 12 18 24, 2 8 14 20, 4 10 16, 6 12, 8 : 12

          012: 0 2 4 6 8, 1 3 5 7, 2 4 6, 3 5, 4 : 9

          012 : 620
          024 : 620
          027 : 170
          035 : 170
          028 : 170
          026 : 170
          036 : 620
        */
        System.out.println(c);
    }

    private long allCombinations(Integer ... eligibleNumbers) {
        board.reset();

        /*while (true)
            for cols
               for rows
                 step up
                 if max: set to 0, go to previous                      0023

         */
        List<Integer> liste = Arrays.asList(eligibleNumbers);
        Integer[] nextEligNumberMap = new Integer[10];
        for (int number : eligibleNumbers) {
            int nesteIndex = liste.indexOf(number) + 1;
            nextEligNumberMap[number] = nesteIndex < liste.size()
                                                ? liste.get(nesteIndex)
                                                : null;
        }
        int maxNumber = eligibleNumbers[eligibleNumbers.length-1];
        long correctCombinations = 0;

        traverse:
        for (int y = width-1; y < width; ) {
            for (int x = width-1; x < width; ) {
                boolean theEnd = x == (width - 1) && y == (width - 1);
                if (board.get(x, y) < 0) board.set(x, y, 0);
                Integer nextEligNumber = nextEligNumberMap[board.get(x, y)];
                if (nextEligNumber != null) {
                    if (theEnd || board.get((x + 1) % width, x < width-1 ? y : y+1) == -1) {
                        board.set(x, y, nextEligNumber);
                        if (!theEnd) {
                            board.set((x + 1) % width, x < width - 1 ? y : y + 1, 0);
                        }
                        if (board.sumsCorrect()) { correctCombinations++; }
                    }
                    if (theEnd)
                        continue;
                }
                else {  // Highest number reached
                    if (x == 0 && y == 0) {
                        break traverse;
                    } else {
                        int prevNumber = -1;
                        do {
                            board.set(x, y, 0);
                            int prevY = x > 0 ? y : y - 1;
                            int prevX = (x - 1 + width) % width;
                            prevNumber = prevY >= 0 ? board.get(prevX, prevY) : -1;
                            x = prevX;
                            y = prevY;
                        }
                        while (prevNumber == maxNumber);
                        if (y >= 0) {
                            board.set(x, y, nextEligNumberMap[board.get(x, y)]);
                            if (board.sumsCorrect()) {
                                correctCombinations++;
                                System.out.println(board.toString().replaceAll("\\n", " "));
                            }
                        } else {
                            break traverse;
                        }
                    }
                }
                x++;
            }
            y++;
        }
        return correctCombinations;
    }
}
