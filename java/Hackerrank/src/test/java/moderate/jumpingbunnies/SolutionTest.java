package moderate.jumpingbunnies;

import org.junit.Test;

import java.util.Arrays;

public class SolutionTest {

    @Test
    public void testIt() {
        System.out.println(Solution.primeFactors(Arrays.asList(14, 12, 8)));
    }

    @Test
    public void testSolve() {
        Solution.solve(Arrays.asList(12, 24, 42).stream());
    }
}
