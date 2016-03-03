package moderate.gridsearch;

import static java.lang.String.valueOf;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * @author trond.
 */
public class SolutionTest {

    @Test
    public void testLongCases() throws Exception {

        final Task task = new Task();
        task.array = new String[1000][1000];
        task.cols = 1000;
        task.rows = 1000;
        task.pattern = new String[300][300];
        task.patternCols = 300;
        task.patternRows = 300;

        final PrimitiveIterator.OfInt digits = new Random().ints(0, 9).iterator();
        for (int i = 0; i < task.cols; i++) {
            for (int j = 0; j < task.rows; j++) {
                task.array[j][i] = valueOf(digits.next());
            }
        }
        for (int i = 0; i < task.patternCols; i++) {
            for (int j = 0; j < task.patternRows; j++) {
                task.pattern[j][i] = valueOf(digits.next());
            }
        }
        final String solved = new Solution().solve(task);
        System.out.println(solved);

    }
}
