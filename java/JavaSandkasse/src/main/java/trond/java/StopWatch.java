package trond.java;

/**
 * @author trond.
 */
class StopWatch {

    private long timeSpent = 0;

    private long lastTimestamp;

    public void start() {
        lastTimestamp = System.nanoTime();
    }

    public void stop() {
        timeSpent += (System.nanoTime() - lastTimestamp);
    }

    public void reset() {
        timeSpent = 0;
    }

    public long getTimeSpent() {
        return timeSpent;
    }
}
