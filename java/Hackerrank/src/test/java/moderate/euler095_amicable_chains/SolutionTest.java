package moderate.euler095_amicable_chains;

import org.junit.Test;

public class SolutionTest {

    @Test
    public void testIt() {
        int divisors = new Solution().sumDivisors2(96);

        System.out.println(divisors);
    }

    @Test
    public void testPrimes() {
        Solution solution = new Solution();
        for (int i = 0; i < 50; i++) {
            int divisor = solution.findNewPrime();
            System.out.println(divisor);
        }
    }
}
