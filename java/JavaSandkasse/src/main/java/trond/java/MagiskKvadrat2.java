package trond.java;

import static java.lang.String.format;
import static java.lang.System.out;

import java.io.File;
import java.util.*;

@SuppressWarnings("checkstyle") public class MagiskKvadrat2 {

    private static boolean debug = true;

    private static final int MILLIS_PER_SECOND = 1000;

    private static final int MILLIS_PER_MINUTE = 60000;

    private static final int MILLIS_PER_HOUR = 3600000;

    private static final int MILLIS_PER_24H = 86400000;

    private static final int squareSize = 4;

    private static final int numberBase = 10;

    private final Integer[][] digitSums = new Integer[squareSize + 1][squareSize];

    {
        {
            for (int i = 0; i < squareSize + 1; i++) {
                digitSums[i] = new Integer[squareSize];
                for (int j = 0; j < squareSize; j++) { digitSums[i][j] = 0;}
            }
        }
    }

    private Map<Integer, Set<Integer[][]>> rowColCombsPerSum;

    private final static StopWatch stopWatchCombinations = new StopWatch();

    private final DebugChecker debugChecker = new DebugChecker();

    /**
     * Last sum before the sum combinations repeat themselves reversed (0->1, 1->3, 2->5, 3->3, 4->1).
     */
    private final int middleSum = (squareSize * (numberBase - 1)) / 2;

    /**
     * Runs the show.
     *
     * @param args They are ignored.
     */
    public static void main(final String[] args) {
        final long timeBefore = System.currentTimeMillis();
        final StopWatch stopWatchMain = new StopWatch();
        stopWatchMain.start();

        new MagiskKvadrat2().solve();

        stopWatchMain.stop();
        System.out.println("Time total: " + stopWatchMain.getTimeSpent());
        System.out.println("Time combs: " + stopWatchCombinations.getTimeSpent());

        final long timeElapsed = System.currentTimeMillis() - timeBefore;
        final long seconds = (timeElapsed % MILLIS_PER_MINUTE) / MILLIS_PER_SECOND;
        final long minutes = (timeElapsed % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
        final long hours = (timeElapsed % MILLIS_PER_24H) / MILLIS_PER_HOUR;

        out.println("... in " + hours + "h, " + minutes + "m and " + seconds + "s");
    }

    private void solve() {
        try {
            new Thread(debugChecker).start();
            final Map<Integer, Long> sumMap = equalSums2();

            out.println("Result:");
            sumMap.entrySet().stream().filter(it -> it.getValue() > 0L)
                  .forEach(entry -> out.println(format("%1$3d: %2$5d", entry.getKey(), entry.getValue())));

            final long doubled = sumMap.values().stream().reduce(Math::addExact).get() * 2;
            out.println(doubled - sumMap.get(middleSum)); //Middle sum counted twice
        }
        finally {
            debugChecker.runForrest = false;
        }
    }

    /**
     * All combinations of ciphers that add up to the given sum.
     *
     * @param sum The given sum.
     * @return Ditto
     */
    private List<int[]> combinations(final int sum) {
        stopWatchCombinations.start();
        final List<int[]> list = new ArrayList<>();
        final int maxCombination = Math.min(sum * 1_000 + 1_000, 10_000);
        for (int i = 0; i < maxCombination; i++) {
            if ((i / 1000) >= numberBase || (i % 1000 / 100) >= numberBase ||
                (i % 100 / 10) >= numberBase || (i % 10) >= numberBase) {
                continue;
            }
            if ((i / 1000) + (i % 1000 / 100) + (i % 100 / 10) + (i % 10) == sum) {
                list.add(new int[] { (i / 1000), (i % 1000 / 100), (i % 100 / 10), (i % 10) });
            }
        }
        stopWatchCombinations.stop();
        return list;
    }

    /**
     * Map of combinations counts per square sum.
     *
     * @return Ditto
     */
    private Map<Integer, Long> equalSums2() {
        final HashMap<Integer, Long> result = new HashMap<>();
        result.put(1, findOneSumBoards());
        for (int sum = 0; sum <= middleSum; sum++) {
            final long numberOfEqualSumBoards = result.put(sum, equalSumBoards(sum));
        }
        return result;
    }

    private Long equalSumBoards(int sum) {
        for (Integer[][] integerLast : rowColCombsPerSum.get(sum - 1)) {
            for (Integer[][] integerDiff : rowColCombsPerSum.get(1)) {
                for (int row = 0; row < 4; row++) {
                    int rowSum = 0;
                    for (int col = 0; col < 4; col++) {
                        rowSum += integerLast[row][col] + integerDiff[row][col];
                    }

                    int colSum = 0;
                    for (int col = 0; col < 4; col++) {
                        rowSum += integerLast[row][col] + integerDiff[row][col];
                    }

                }
            }
        }
        return 0L;
    }

    private long findOneSumBoards() {
        final List<int[]> combinationsForSum = combinations(1);
        final Integer[][] combinations = new Integer[combinationsForSum.size()][squareSize];
        combinationsForSum.toArray(combinations);

        digitSums[0] = new Integer[] { 0, 0, 0, 0 };
        long numberOfEqualSumBoards = 0L;
        Integer[] row1;
        Integer[] row2;
        Integer[] row3;
        Integer[] row4;

        for (final Integer[] combination1 : combinations) {
            row1 = combination1;
            if (!advanceDigitSums(row1, 1, 1)) {
                continue;
            }
            for (final Integer[] combination2 : combinations) {
                row2 = combination2;
                if (!advanceDigitSums(row2, 1, 2)) {
                    continue;
                }
                for (final Integer[] combination3 : combinations) {
                    row3 = combination3;
                    if (!advanceDigitSums(row3, 1, 3)) {
                        continue;
                    }
                    row4 = calculateLastRow(1);

                    if (rowOkCombination(row4, 1)) {
                        addMatch(1, row1, row2, row3, row4);
                        if (diagonalsEqual(1, row1, row2, row3, row4)) {
                            numberOfEqualSumBoards++;
                        }
                    }
                }
            }
        }
        System.out.println();
        return numberOfEqualSumBoards;
    }

    private void addMatch(int sum, Integer[] row1, Integer[] row2, Integer[] row3, Integer[] row4) {
        Set<Integer[][]> integers = rowColCombsPerSum.get(sum);
        if (integers == null)
            rowColCombsPerSum.put(sum, new TreeSet<>());

        rowColCombsPerSum.get(sum).add(new Integer[][] { row1, row2, row3, row4 });
    }

    private boolean rowOkCombination(final Integer[] row, final int sum) {
        int sumRow = 0;
        for (Integer number : row) {
            sumRow += number;
        }
        if (sumRow != sum)
            return false;
        for (final int number : row) {
            if (number >= numberBase) {
                return false;
            }
        }
        return true;
    }

    private Integer[] calculateLastRow(final int sum) {
        final Integer[] integers = new Integer[4];
        for (int i = 0; i < 4; i++) {
            integers[i] = sum - digitSums[3][i];
        }
        return integers;
    }

    private boolean diagonalsEqual(final int sum, final Integer[] row1, final Integer[] row2, final Integer[] row3,
                                   final Integer[] row4) {
        final int diagSum1 = row1[0] + row2[1] + row3[2] + row4[3];
        if (diagSum1 != sum)
            return false;
        final int diagSum2 = row4[0] + row3[1] + row2[2] + row1[3];
        return diagSum2 == sum;
    }

    /**
     * Updates digitSums if the column sums are OK.
     *
     * @param row ciphers in row
     * @param sum desired square sum
     * @return true if updated, otherwise false.
     */
    private boolean advanceDigitSums(final Integer[] row, final int sum, final int rowIndex) {
        final int[] sums = new int[squareSize];
        for (int i = 0; i < squareSize; i++) {
            sums[i] = digitSums[rowIndex - 1][i] + row[i];
            if (sums[i] > sum) {
                return false;
            }
        }
        for (int i = 0; i < squareSize; i++) {
            digitSums[rowIndex][i] = sums[i];
        }
        return true;
    }

    @SuppressWarnings("unused")
    void printBoard(final int[][] board) {
        debug("|");
        for (int i = 0; i < squareSize; i++) {
            for (int j = 0; j < squareSize; j++) {
                debug(board[i][j]);
            }
        }
        debugln();
    }

    int compareIntegerArrays(final Integer[][] a, final Integer[][] b) {
        for (int outerIndex = 0; outerIndex < a.length; outerIndex++) {
            for (int innerIndex = 0; innerIndex < a[0].length; innerIndex++) {
                if (a[outerIndex][innerIndex] != b[outerIndex][innerIndex]) {
                    return a[outerIndex][innerIndex] - b[outerIndex][innerIndex];
                }
            }
        }
        return 0;
    }

    private void debugln() {
        if (debug)
            out.println();
    }

    @SuppressWarnings("unused")
    private void debugln(final Object s) {
        if (debug)
            out.println(s);
    }

    private void debug(final Object s) {
        if (debug)
            out.print(s);
    }

    class DebugChecker implements Runnable {

        public boolean runForrest = true;

        @Override
        public void run() {
            while (runForrest) {
                MagiskKvadrat2.debug = new File("./debug.flag").exists();
                try {
                    Thread.sleep(5000);
                }
                catch (final InterruptedException ignored) {
                }
            }
        }
    }

}

