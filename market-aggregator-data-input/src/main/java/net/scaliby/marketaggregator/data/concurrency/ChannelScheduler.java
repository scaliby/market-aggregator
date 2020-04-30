package net.scaliby.marketaggregator.data.concurrency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
public class ChannelScheduler<K> {

    private final Executor executor;

    private final Map<K, ChannelRunner> runners = new HashMap<>();

    @Setter
    private int queueCapacity = 100_000;

    @SneakyThrows
    public void push(ChannelRunnable<K> job) {
        synchronized (runners) {
            boolean alreadyRunning = runners.containsKey(job.getChannel());
            ChannelRunner channelRunner = runners.computeIfAbsent(job.getChannel(), ChannelRunner::new);
            channelRunner.push(job);
            if (!alreadyRunning) {
                executor.execute(channelRunner);
            }
        }
    }

    private void finalizeQueueRunner(ChannelRunner runner) {
        synchronized (runners) {
            if (runner.hasData()) {
                executor.execute(runner);
            } else {
                runners.remove(runner.getKey());
            }
        }
    }

    @RequiredArgsConstructor
    private final class ChannelRunner implements Runnable {

        @Getter
        private final K key;
        private final BlockingQueue<Runnable> workload = new ArrayBlockingQueue<>(queueCapacity);

        @Override
        public void run() {
            while (hasData()) {
                Runnable elem = workload.poll();
                if (elem == null) {
                    break;
                }
                elem.run();
            }
            finalizeQueueRunner(this);
        }

        @SneakyThrows
        void push(ChannelRunnable<K> data) {
            workload.put(data);
        }

        boolean hasData() {
            return workload.size() > 0;
        }

    }

}
