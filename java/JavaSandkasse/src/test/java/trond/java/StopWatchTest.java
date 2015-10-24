package trond.java;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author trond.
 */
public class StopWatchTest {

    final int nanosPerSecond = 1_000_000_000;

    @Test
    public void testStartStopAndReset() throws Exception {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Thread.sleep(1200); //sleep 1 second

        stopWatch.stop();

        Assert.assertTrue(stopWatch.getTimeSpent() > nanosPerSecond);

        stopWatch.reset();

        Assert.assertEquals(0, stopWatch.getTimeSpent());
    }
}
