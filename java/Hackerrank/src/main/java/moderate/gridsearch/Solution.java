package moderate.gridsearch;

import static java.lang.String.format;

import java.io.StringReader;
import java.util.*;

/**
 * @author trond.
 */
public class Solution {
    static final String input = "1\n" +
                                "3 3\n" +
                                "345\n" +
                                "126\n" +
                                "472\n" +
                                "2 2\n" +
                                "26\n" +
                                "72\n";

    private boolean DEBUG = false;

    public static void main(final String[] args) {
        final Solution solution = new Solution();

        final Scanner scanner = new Scanner(System.in);//new StringReader(input));
        final List<Task> tasks = solution.readTasks(scanner);
        try {
            tasks.forEach((task) -> {
                final String solved = solution.solve(task);
                System.out.println(solved);
            });
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    List<Task> readTasks(final Scanner scanner) {
        final int caseCount = scanner.nextInt(); scanner.nextLine();
        final List<Task> tasks = new ArrayList<>();

        for (int caseNr = 0; caseNr < caseCount; caseNr ++) {
            final Task task = new Task();
            task.rows = scanner.nextInt();
            task.cols = scanner.nextInt(); scanner.nextLine();

            task.array = new String[task.rows][task.cols];
            for (int row = 0; row < task.rows; row++) {
                task.array[row] = scanner.nextLine().split("");
            }
            task.patternRows = scanner.nextInt();
            task.patternCols = scanner.nextInt(); scanner.nextLine();

            task.pattern = new String[task.patternRows][task.patternCols];
            for (int i = 0; i < task.patternRows; i++) {
                task.pattern[i] = scanner.nextLine().split("");
            }
            tasks.add(task);
        }
        return tasks;
    }

    String solve(final Task task) {
        final int startCol = 0;
        List<Hit> nextHits;
        final List<Hit> hits = new ArrayList<>();
        for (int row = 0; row < task.rows; row++) {
            nextHits = new ArrayList<>();
            for (int col = startCol; col < task.cols; col++) {
                if (task.array[row][col].equals(task.pattern[0][0])) {
                    hits.add(new Hit(col, 1, 0));
                }

                final List<Hit> toRemove = new ArrayList<>();
                for (final Hit hit : hits) {
                    if (col != hit.startCol + hit.count)
                        continue;
                    debugLn(format("task.array[%d][%d].equals(task.pattern[%d][%d])",
                                   row, col, hit.patternRow, hit.count));

                    if (task.array[row][col].equals(task.pattern[hit.patternRow][hit.count])) {
                        hit.count++;
                        if (hit.count == task.patternCols) {
                            toRemove.add(hit);
                            final Hit newHit = new Hit(hit.startCol, 0, hit.patternRow + 1);
                            nextHits.add(newHit);
                            if (newHit.patternRow == task.patternRows)
                                return "YES";
                        }
                    }
                }
                toRemove.forEach(hits::remove);
            }
            hits.clear();
            hits.addAll(nextHits);
        }
        return "NO";
    }

    private void debugLn(final String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}

class Hit {
    int startCol;
    int count;
    int patternRow;
    public Hit(final int startCol, final int count, final int patternRow) {
        this.startCol = startCol;
        this.count = count;
        this.patternRow = patternRow;
    }
}

class Task {
    public int rows;
    public int cols;
    public int patternRows;
    public int patternCols;
    public String[][] array;
    public String[][] pattern;
}

class Tester {

    public static void foo() {
        System.out.println("");
    }
}
