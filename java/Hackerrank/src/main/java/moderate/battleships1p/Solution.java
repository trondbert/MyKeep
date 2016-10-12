package moderate.battleships1p;

import static java.lang.Math.ceil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Solution {

    final Cell[][] field = new Cell[11][11];

    final List<Cell> hits = new ArrayList<>();

    Cell bestCandidate;

    int longestOpenSpan = 0;

    Solution() {
    }

    Solution(final String inputField) {
        reset(inputField);
    }

    void reset(final String inputField) {
        final String[] rows = inputField.split("\n");
        for (int row = 0; row < 10; row++) {
            final String[] split = rows[row].split("");
            for (int col = 0; col < 10; col++) {
                field[row][col] = new Cell(row, col, Status.of(split[col]));
            }
            field[row][10] = new Cell(row, 10, Status.MISS);
        }
        for (int i = 0; i < 11; i++) {
            field[10][i] = new Cell(10, i, Status.MISS);
        }
        bestCandidate = field[9][9];
        hits.clear();
        longestOpenSpan = 0;
    }

    public static void main(final String[] args) {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            final StringBuilder builder = new StringBuilder("");
            br.readLine();
            String line = br.readLine();
            while (line != null && !line.equals("")) {
                builder.append(line).append("\n");
                line = br.readLine();
            }
            Solution solution = new Solution(builder.toString());
            System.out.println(solution.nextMove(solution::bestCandidate));
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    String nextMove() {
        return nextMove(this::bestCandidate);
    }

    String nextMove(Supplier<String> getit) {
        updateField();

        if (hits.isEmpty())
            return getit.get();

        final String sds;
        try {
            sds = candidateFromExistingHits();
            return sds;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String bestCandidate() {
        return bestCandidate.row + " " + bestCandidate.col;
    }

    String randomCandidate() {
        int row;
        int col;
        do {
            row = new Random().nextInt(10);
            col = new Random().nextInt(10);
        } while (field[row][col].status != Status.OPEN);
        return row + " " + col;
    }

    private String candidateFromExistingHits() {

        final Function<List<Cell>, String> whereShipContinues = (neighbors) -> {
            for (int i = 0; i < neighbors.size(); i++)
                if (neighbors.get(i) != null && neighbors.get(i).status == Status.HIT) {
                    final Cell opposite = neighbors.get((i + 2) % 4);
                    if (opposite != null && opposite.status == Status.OPEN)
                        return opposite.row + " " + opposite.col;
                }
            return null;
        };
        final Function<List<Cell>, String> firstOpenSpace = (neighbors) -> {
            for (final Cell neighbor : neighbors)
                if (neighbor != null && neighbor.status == Status.OPEN)
                    return neighbor.row + " " + neighbor.col;
            return null;
        };
        for (final Cell hit : hits) {
            final String whereToHit = whereShipContinues.apply(neighbors(hit));
            if (whereToHit != null)
                return whereToHit;
        }
        for (final Cell hit : hits) {
            final String whereToHit = firstOpenSpace.apply(neighbors(hit));
            if (whereToHit != null)
                return whereToHit;
        }
        return null;
    }

    private void updateField() {
        for (final Cell[] cellRow : field) {
            for (final Cell cell : cellRow) {
                evaluateCell(cell);
            }
        }
    }

    private Cell evaluateCell(final Cell cell) {
        if (cell.status.equals(Status.HIT)) {
            hits.add(cell);
        }
        if (!hits.isEmpty()) {
            return null;
        }
        final List<Cell> neighbors = neighbors(cell);
        final Cell upCell = neighbors.get(0);
        final Cell leftCell = neighbors.get(3);

        if (cell.status == Status.OPEN) {
            if (upCell != null && upCell.status == Status.OPEN) {
                cell.verticalLength = upCell.verticalLength + 1;
            }
            if (leftCell != null && leftCell.status == Status.OPEN) {
                cell.horizontalLength = leftCell.horizontalLength + 1;
            }
        }
        else if (cell.status != Status.OPEN) {
            if (upCell != null && upCell.status == Status.OPEN) {
                final int candidateRow = (int) (cell.row - ceil(upCell.verticalLength / 2.0));
                final Cell middleCell = field[candidateRow][cell.col];
                if (upCell.verticalLength > longestOpenSpan) {
                    longestOpenSpan = upCell.verticalLength;
                    bestCandidate = middleCell;
                }
            }
            if (leftCell != null && leftCell.status == Status.OPEN) {
                final int candidateCol = (int) (cell.col - ceil(leftCell.horizontalLength / 2.0));
                final Cell middleCell = field[cell.row][candidateCol];
                if (leftCell.horizontalLength > longestOpenSpan) {
                    longestOpenSpan = leftCell.horizontalLength;
                    bestCandidate = middleCell;
                }
            }
        }
        return bestCandidate;
    }

    private List<Cell> neighbors(final Cell cell) {
        final int[] dx = new int[] { 0, 1, 0, -1 };
        final int[] dy = new int[] { -1, 0, 1, 0 };
        final List<Cell> result = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            final int x = cell.col + dx[i];
            final int y = cell.row + dy[i];
            if (x >= 0 && x < 10 && y >= 0 && y < 10)
                result.add(field[y][x]);
            else
                result.add(null);
        }
        return result;
    }

    static final String exampleField = "----h-----\n" +
                                       "----h-----\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n" +
                                       "----------\n";

    @SuppressWarnings("unused")
    private static void pln(final Object s) {
        System.out.println(s);
    }

}


enum Status {
    HIT("h"), DESTROYED("d"), MISS("m"), OPEN("-");

    final String stringValue;

    Status(final String stringValue) {
        this.stringValue = stringValue;
    }

    static Status of(final String stringValue) {
        for (final Status s : Status.values()) {
            if (s.stringValue.equals(stringValue))
                return s;
        }
        return null;
    }
}


class Cell {

    int col;

    int row;

    int verticalLength = 1;

    int horizontalLength = 1;

    Status status;

    Cell(final int row, final int col, final Status status) {
        this.col = col;
        this.row = row;
        this.status = status;
    }

    @Override
    public String toString() {
        return row + ", " + col + ": " + status.stringValue;
    }
}
