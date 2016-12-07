package hackerrank.moderate.gridsearch;

import static java.lang.String.format;

import java.io.*;
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    Scanner getScanner(final InputStream input) throws IOException {
        final byte[] data = new byte[10000000];
        input.read(data);
        input.close();

        final String str = new String(data, "UTF-8");
        return new Scanner(new StringReader(str));
    }

    List<Task> readTasks(final Scanner scanner) {
        final int caseCount = scanner.nextInt(); scanner.nextLine();
        final List<Task> tasks = new ArrayList<>();

        for (int caseNr = 0; caseNr < caseCount; caseNr ++) {
            final Task task = new Task();
            task.rows = scanner.nextInt();
            task.cols = scanner.nextInt(); scanner.nextLine();

            task.lines = new String[task.rows];
            for (int row = 0; row < task.rows; row++) {
                task.lines[row] = scanner.nextLine();
            }
            task.patternRows = scanner.nextInt();
            task.patternCols = scanner.nextInt(); scanner.nextLine();

            task.pattern = new String[task.patternRows];
            for (int i = 0; i < task.patternRows; i++) {
                task.pattern[i] = scanner.nextLine();
            }
            tasks.add(task);
        }
        return tasks;
    }

    String solve(final Task task) {
        final int lastColForHitStart = task.cols - task.patternCols;
        final int startCol = 0;
        final char patternStart = task.pattern[0].charAt(0);
        List<Hit> nextHits;
        final List<Hit> hits = new ArrayList<>();
        boolean hitsOnRowPossible = true;
        for (int row = 0; row < task.rows; row++) {
            hitsOnRowPossible &= row + task.patternRows <= task.rows;
            if (!hitsOnRowPossible && hits.isEmpty())
                break;
            nextHits = new ArrayList<>();
            for (int col = startCol; col < task.cols; col++) {
                final char currentDigit = task.lines[row].charAt(col);;
                final List<Hit> toRemove = new ArrayList<>();
                for (final Hit hit : hits) {
                    if (col != hit.startCol + hit.count) continue;
                    if (currentDigit == task.pattern[hit.patternRow].charAt(hit.count)) {
                        hit.count++;
                        if (hit.count == task.patternCols) {
                            if (hit.patternRow + 1 == task.patternRows)
                                return "YES";
                            toRemove.add(hit);
                            nextHits.add(new Hit(hit.startCol, 0, hit.patternRow + 1));
                        }
                        continue;
                    }
                    toRemove.add(hit);
                }
                toRemove.forEach(hits::remove);
                if (hitsOnRowPossible && col <= lastColForHitStart
                    && (currentDigit == patternStart)) {
                    hits.add(new Hit(col, 1, 0));
                }
            }
            hits.clear();
            hits.addAll(nextHits);
        }
        return "NO";
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
    public String[] lines;
    public String[] pattern;
}
