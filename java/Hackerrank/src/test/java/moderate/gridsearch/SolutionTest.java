package moderate.gridsearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

/**
 * @author trond.
 */
public class SolutionTest {

    @Test
    public void testLongCases() throws Exception {
        final InputStream inputFile = getClass().getResourceAsStream("/testcase5_gridsearch.txt");
        final InputStreamReader inputStreamReader = new InputStreamReader(inputFile);

        final Solution solution = new Solution();
        final long startReadTimer = System.currentTimeMillis();
        //final List<Task> tasks = solution.readTasks(new Scanner(new BufferedReader(inputStreamReader)));
        final List<Task> tasks = solution.readTasks(solution.getScanner(inputFile));
        final long timeReading = System.currentTimeMillis() - startReadTimer;
        System.out.println("time reading file: " + timeReading);

        System.out.println("Starting");
        long timeCombined = 0;
        final long runCount = 1;
        for (int i = 0; i < runCount; i++) {
            final long start = System.currentTimeMillis();
            for (final Task task : tasks) {
                final String solved = solution.solve(task);
                System.out.println(solved);
            }
            final long timeUsed = System.currentTimeMillis() - start;
            System.out.println(timeUsed + " ms");
            timeCombined += timeUsed;
        }
        System.out.println("Avg: " + (timeCombined / runCount));
    }

    @Test
    public void testSmallCase() {
        final Task task = new Task();
        task.rows = 5;
        task.cols = 5;
        task.patternRows = 3;
        task.patternCols = 3;
        task.lines = new String[]  {"12345",
                                    "22345",
                                    "12344",
                                    "13455",
                                    "16785"};

        task.pattern = new String[] {"234",
                                     "345",
                                     "678"};
        System.out.println(
                new Solution().solve(task));

        for (int i = 0; i < 78; i++) {
            System.out.print(i + " ");
            System.out.println((char)i + (char)i);
        }
        System.out.println("4234".charAt(2) + "5432534".charAt(2));
        System.out.println("4234".charAt(2));
        System.out.println("5432534".charAt(2));
        System.out.println((char)64);
    }

}
