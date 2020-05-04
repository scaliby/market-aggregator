package net.scaliby.marketaggregator.core.concurrency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.scaliby.marketaggregator.core.common.ConcurrentExecutionsCounter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChannelSchedulerTest {

    @Test
    public void runningTwoJobsForTheSameKey_executesThemSequentially() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ChannelScheduler<String> channelScheduler = new ChannelScheduler<>(executorService);
        ConcurrentExecutionsCounter counter = new ConcurrentExecutionsCounter();
        TestingChannelRunnable firstJob = new TestingChannelRunnable("BTC_USD", counter);
        TestingChannelRunnable secondJob = new TestingChannelRunnable("BTC_USD", counter);

        // when
        channelScheduler.push(firstJob);
        channelScheduler.push(secondJob);
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // then
        assertTrue(firstJob.isExecuted());
        assertTrue(secondJob.isExecuted());
        assertEquals(1, counter.getMaxCount());
    }

    @Test
    public void runningTwoJobsForDifferentKeys_executesThemInParallel() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ChannelScheduler<String> channelScheduler = new ChannelScheduler<>(executorService);
        ConcurrentExecutionsCounter counter = new ConcurrentExecutionsCounter();
        TestingChannelRunnable firstJob = new TestingChannelRunnable("BTC_USD", counter);
        TestingChannelRunnable secondJob = new TestingChannelRunnable("ETH_USD", counter);

        // when
        channelScheduler.push(firstJob);
        channelScheduler.push(secondJob);
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // then
        assertTrue(firstJob.isExecuted());
        assertTrue(secondJob.isExecuted());
        assertEquals(2, counter.getMaxCount());
    }

    @Test(timeout = 2000L)
    public void runningJobForTheSameKeyTwice_executesItSuccessfullyWithoutDeadlock_forQueueCapacityEqualToOne() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ChannelScheduler<String> channelScheduler = new ChannelScheduler<>(executorService);
        channelScheduler.setQueueCapacity(1);
        ConcurrentExecutionsCounter counter = new ConcurrentExecutionsCounter();
        TestingChannelRunnable firstJob = new TestingChannelRunnable("BTC_USD", counter);
        TestingChannelRunnable secondJob = new TestingChannelRunnable("ETH_USD", counter);

        // when
        channelScheduler.push(firstJob);
        channelScheduler.push(secondJob);
        channelScheduler.push(secondJob);
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        // then
        assertTrue(firstJob.isExecuted());
        assertTrue(secondJob.isExecuted());
    }

    @RequiredArgsConstructor
    private static class TestingChannelRunnable implements ChannelRunnable<String> {
        @Getter
        private final String channel;
        private final ConcurrentExecutionsCounter concurrentExecutionsCounter;
        @Getter
        private volatile boolean executed = false;

        @Override
        @SneakyThrows
        public void run() {
            concurrentExecutionsCounter.executionStarted();
            Thread.sleep(100L);
            concurrentExecutionsCounter.executionFinished();
            executed = true;
        }
    }

}