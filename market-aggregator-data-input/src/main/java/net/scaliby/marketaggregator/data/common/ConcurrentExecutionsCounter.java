package net.scaliby.marketaggregator.data.common;

public class ConcurrentExecutionsCounter {

    private final Object lock = new Object();
    private volatile int count = 0;
    private volatile int maxCount = 0;

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
