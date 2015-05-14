import com.sun.istack.internal.NotNull;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;

public class AveragerTest {

    @Test
    public void calculates_average_of_three_elements() throws Exception {

        Averager averager = Arrays.asList(1, 2, 3).stream()
                .collect(Averager::new, Averager::accept, Averager::combine);

        Assertions.assertThat(averager.average()).isEqualTo(2);
    }

    @Test
    public void all_match_returns_true_when_all_are_equal() throws Exception {
        Assertions.assertThat(Arrays.asList(2, 2, 2).stream().allMatch(i -> i == 2)).isTrue();
    }

    @Test
    public void all_match_returns_true_when_all_satisfy_condition() throws Exception {
        Assertions.assertThat(Arrays.asList(2, 4, 6).stream().allMatch(i -> i > 1)).isTrue();
    }

    @Test
    public void diffy() throws Exception {
        int generator = 3;
        int aExp = 8;
        int bExp = 7;
        int modBase = 13;

        double aPower = Math.pow(generator, aExp);
        double bPower = Math.pow(generator, bExp);
        int aContribution = (int) (aPower % modBase);
        int bContribution = (int) (bPower % modBase);

        int aResult = (int) Math.pow(bContribution, aExp) % modBase;
        int bResult = (int) Math.pow(aContribution, bExp) % modBase;

        Assertions.assertThat(aResult).isEqualTo(bResult);

        boolean truth = 1 == 1;

        @Writer String foo = null;

        @NotNull String foo2 = null;

        if (truth) return;

        System.out.println("Foo");
    }

}
