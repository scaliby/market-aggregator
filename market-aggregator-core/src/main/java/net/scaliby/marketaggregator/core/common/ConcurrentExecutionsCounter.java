package net.scaliby.marketaggregator.core.common;

public class ConcurrentExecutionsCounter {

    private final Object lock = new Object();
    private int count = 0;
    private int maxCount = 0;

    public int getMaxCount() {
        synchronized (lock) {
            return maxCount;
        }
    }

    public void executionStarted() {
        synchronized (lock) {
            count++;
            maxCount = Math.max(maxCount, count);
        }
    }

    public void executionFinished() {
        synchronized (lock) {
            count--;
            maxCount = Math.max(maxCount, count);
        }
    }

}
