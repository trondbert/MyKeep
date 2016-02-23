package trond.java;

import static java.lang.Math.max;
import static java.lang.System.out;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("checkstyle") public class MagiskKvadrat2 {

    private static final Integer[][] digitSums = new Integer[4 + 1][4];

    /**
     * Runs the show.
     *
     * @param args They are ignored.
     */
    public static void main(final String[] args) {
        final List<List<Integer>> cellList = Stream.iterate(0, (x) -> x + 1).limit(4).collect(
                mapping((num) -> new ArrayList<Integer>() {{ add(num); }}, toList()));

        final Function<List<List<Integer>>, List<List<Integer>>> fleshOut = MagiskKvadrat2::fleshOut;
        final List<List<Integer>> fullList = fleshOut.andThen(fleshOut).andThen(fleshOut).apply(cellList);

        final List<Integer> possibleRow = Arrays.asList(4, 5, 6, 8);
        final int sumPossibleRow = possibleRow.stream().reduce((x, y) -> x + y).get();

        final List<List<Integer>> possibleRows =
                fullList.stream().filter(list -> {return list.stream().reduce(0, (x, y) -> x + y) == 4;})
                        .map(contributions -> {
                            return Stream.iterate(0, x-> x + 1).limit(contributions.size())
                                         .map(i -> possibleRow.get(i) * contributions.get(i))
                                         .collect(toList());
                             }
                        ).filter( row -> row.stream().reduce(0, (x,y) -> x+y) == sumPossibleRow)
                        .collect(toList());

        possibleRows.stream().map(cells ->
            IntStream.range(0,4).flatMap(i ->
                    IntStream.generate( ()-> possibleRow.get(i)).limit(cells.get(i) / possibleRow.get(i)))
                    .mapToObj(String::valueOf)
                     .collect(Collectors.joining(" "))
        ).forEach(System.out::println);

        final int maxCellValue = 8;

        final int[] cells = { 1, 2, 3, 4 };
        do {
            final List<int[]> permutations = permutations(cells);
            out.print(" " + magicSquaresCount(permutations));
            out.println(" " + IntStream.of(cells).mapToObj(String::valueOf).collect(joining(" ")));
        } while (increaseCellValues(cells, maxCellValue));

        final List<int[]> permutations = permutations(new int[] { 1, 2, 3, 4 });
        final List<int[]> permutations2 = permutations(new int[] { 2, 4, 6, 8 });
        out.println("");
    }

    private static int magicSquaresCount(final List<int[]> possibleRows) {
        digitSums[0] = new Integer[] { 0, 0, 0, 0 };
        int numberOfEqualSumBoards = 0;
        int[] row1;
        int[] row2;
        int[] row3;
        int[] row4;

        final int[] row = possibleRows.get(0);
        final int sum = row[0] + row[1] + row[2] + row[3];

        for (final int[] combination1 : possibleRows) {
            row1 = combination1;
            if (!advanceDigitSums(row1, sum, 1)) {
                continue;
            }
            for (final int[] combination2 : possibleRows) {
                row2 = combination2;
                if (!advanceDigitSums(row2, sum, 2)) {
                    continue;
                }
                for (final int[] combination3 : possibleRows) {
                    row3 = combination3;
                    if (!advanceDigitSums(row3, sum, 3)) {
                        continue;
                    }
                    for (final int[] combination4 : possibleRows) {
                        row4 = combination4;
                        if (!advanceDigitSums(row4, sum, 4)) {
                            continue;
                        }
                        if (rowOkCombination(row4, sum) && diagonalsEqual(sum, row1, row2, row3, row4)) {
                            //debugComb(numberOfEqualSumBoards, row1, row2, row3, row4, sum, cellsTemp);
                            numberOfEqualSumBoards++;
                        }
                    }
                }
            }
        }
        return numberOfEqualSumBoards;
    }

    private static void debugComb(int numberOfEqualSumBoards, final int[] row1, final int[] row2, final int[] row3,
                                  final int[] row4, final int sum, final ArrayList<Integer> cellsTemp) {
        if (cellsTemp.containsAll(Arrays.asList(1, 2, 3, 4))) {
            out.print(IntStream.of(row1).map(e -> e * 2).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.print(IntStream.of(row2).map(e -> e * 2).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.print(IntStream.of(row3).map(e -> e * 2).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.println(IntStream.of(row4).map(e -> e * 2).mapToObj(String::valueOf).collect(joining(" ")) + " ");
        }
        else if (cellsTemp.containsAll(Arrays.asList(2, 4, 6, 8))) {
            out.print(IntStream.of(row1).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.print(IntStream.of(row2).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.print(IntStream.of(row3).mapToObj(String::valueOf).collect(joining(" ")) + " ");
            out.println(IntStream.of(row4).mapToObj(String::valueOf).collect(joining(" ")) + " ");
        }
    }

    private static boolean rowOkCombination(final int[] row, final int sum) {
        final int sumLastRow = IntStream.of(row).reduce(Math::addExact).getAsInt();
        if (sumLastRow != sum)
            return false;
        for (final int number : row) {
            if (number >= 10) {
                return false;
            }
        }
        return true;
    }

    static List<List<Integer>> fleshOut(final List<List<Integer>> list) {
        return list.stream().flatMap((simpleList) -> Stream.iterate(0, (x) -> x + 1).limit(4)
                                                           .map(num -> new ArrayList<Integer>(simpleList) {{ add(num); }}))
                   .collect(toList());
    }

    private static int[] calculateLastRow(final int sum) {
        final int[] integers = new int[4];
        for (int i = 0; i < 4; i++) {
            integers[i] = sum - digitSums[3][i];
        }
        return integers;
    }

    private static boolean diagonalsEqual(final int sum, final int[] row1, final int[] row2, final int[] row3, final int[] row4) {
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
    private static boolean advanceDigitSums(final int[] row, final int sum, final int rowIndex) {
        final int[] sums = new int[4];
        for (int i = 0; i < 4; i++) {
            sums[i] = digitSums[rowIndex - 1][i] + row[i];
            if (sums[i] > sum) {
                return false;
            }
        }
        for (int i = 0; i < 4; i++) {
            digitSums[rowIndex][i] = sums[i];
        }
        return true;
    }

    private static List<int[]> permutations(final int[] cells) {
        final ArrayList<int[]> permutations = new ArrayList<>();

        for (int i = 0; i <= 3210; i++) {
            final int cell1 = i / 1000;
            final int cell2 = i % 1000 / 100;
            final int cell3 = i % 100 / 10;
            final int cell4 = i % 10;

            if (max(max(max(cell1, cell2), cell3), cell4) <= 3 &&
                cell1 != cell2 && cell1 != cell3 && cell1 != cell4 &&
                cell2 != cell3 && cell2 != cell4 &&
                cell3 != cell4) {
                final int[] permutation = new int[4];
                permutation[0] = cells[cell1];
                permutation[1] = cells[cell2];
                permutation[2] = cells[cell3];
                permutation[3] = cells[cell4];

                permutations.add(permutation);
            }
        }
        return permutations;
    }

    private static boolean increaseCellValues(final int[] cells, final int maxCellValue) {
        for (int i = 3; i >= 0; i--) {
            cells[i] = max(cells[i] + 1, cells[0] + i);
            if (cells[i] <= maxCellValue - (3 - i)) {
                for (int j = i + 1; j < 4; j++) {
                    cells[j] = cells[j - 1] + 1;
                }
                break;
            }
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

}

