
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class KnowitJulekalenderLuke21 {

    private final long[][] numbers;
    private final Case taskCase;
    private int taskSize;
    private int maxIndex;

    public static void main(String[] args) {
        try {
            System.out.println("A " + new KnowitJulekalenderLuke21(TopDownCase.class).solve());
            System.out.println("B " + new KnowitJulekalenderLuke21(LeftUpCase.class).solve());
            System.out.println("C " + new KnowitJulekalenderLuke21(RightUpCase.class).solve());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    KnowitJulekalenderLuke21(Class<? extends Case> caseClass) throws ReflectiveOperationException {
        this.taskCase = caseClass.getDeclaredConstructor(this.getClass()).newInstance(this);

        List<String> lines = tryThis(() -> Files.readAllLines(Paths.get("/tmp/inputKnowit.txt"), UTF_8));
        taskSize = lines.size();
        maxIndex = taskSize -1;

        numbers = lines.stream().map(line -> {
            long[] row = new long[taskSize];
            int col = 0;
            for (String num : line.split(" "))
                row[col++] = Long.valueOf(num);
            return row;
        }).collect(toList()).toArray(new long[taskSize][]);
    }

    private long solve() throws IOException {
        taskCase.goToStartPos();
        boolean hasNextRow = true;
        do {
            taskCase.calculateCurrentPos();
            boolean next = taskCase.goToNextInRow();
            if (!next)
                hasNextRow = taskCase.startNextRow();
        }
        while(hasNextRow);
        return taskCase.solution();
        /*for (long[] line : numbers) {
            for (long num : line) {
                System.out.print(num + " ");
            }
            System.out.println();
        }*/
    }

    Position pos(int row, int col) {
        return new Position(row, col);
    }
    class Position {
        public final int row;
        public final int col;
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    abstract class Case {
        Position currentPos;

        abstract void goToStartPos();
        public abstract void calculateCurrentPos();

        public abstract boolean goToNextInRow();

        public abstract boolean startNextRow();

        public abstract long solution();
    }

    class TopDownCase extends Case {

        @Override
        void goToStartPos() {
            currentPos = pos(maxIndex - 1, 0);
        }

        @Override
        public void calculateCurrentPos() {
            int col = currentPos.col;
            int row = currentPos.row;
            numbers[row][col] += Long.max(numbers[row + 1][col], numbers[row + 1][col + 1]);
        }

        @Override
        public boolean goToNextInRow() {
            int nextCol = currentPos.col + 1;
            if (nextCol > currentPos.row)
                return false;
            currentPos = pos(currentPos.row, nextCol);
            return true;
        }

        @Override
        public boolean startNextRow() {
            if (currentPos.row == 0)
                return false;
            currentPos = pos(currentPos.row - 1, 0);
            return true;
        }

        @Override
        public long solution() {
            return numbers[0][0];
        }
    }

    class LeftUpCase extends Case {

        @Override
        void goToStartPos() {
            currentPos = pos(1, 1);
        }

        @Override
        public void calculateCurrentPos() {
            int col = currentPos.col;
            int row = currentPos.row;
            numbers[row][col] += Long.max(numbers[row - 1][col - 1], numbers[row][col - 1]);
        }

        @Override
        public boolean goToNextInRow() {
            int nextRow = currentPos.row + 1;
            if (nextRow > maxIndex)
                return false;
            currentPos = pos(nextRow, currentPos.col);
            return true;
        }

        @Override
        public boolean startNextRow() {
            int nextCol = currentPos.col + 1;
            if (nextCol > maxIndex)
                return false;
            currentPos = pos(nextCol, nextCol);
            return true;
        }

        @Override
        public long solution() {
            return numbers[maxIndex][maxIndex];
        }
    }

    class RightUpCase extends Case {

        @Override
        void goToStartPos() {
            currentPos = pos(1, 0);
        }

        @Override
        public void calculateCurrentPos() {
            int col = currentPos.col;
            int row = currentPos.row;
            numbers[row][col] += Long.max(numbers[row][col + 1], numbers[row - 1][col]);
        }

        @Override
        public boolean goToNextInRow() {
            if (currentPos.row == maxIndex)
                return false;
            currentPos = pos(currentPos.row + 1, currentPos.col + 1);
            return true;
        }

        @Override
        public boolean startNextRow() {
            int nextRow = currentPos.row - currentPos.col + 1;
            if (nextRow > maxIndex)
                return false;
            currentPos = pos(nextRow, 0);
            return true;
        }

        @Override
        public long solution() {
            return numbers[maxIndex][0];
        }
    }

    private static <T> T tryThis(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

@FunctionalInterface
interface ThrowingSupplier<T> {
    T get() throws Exception;
}

