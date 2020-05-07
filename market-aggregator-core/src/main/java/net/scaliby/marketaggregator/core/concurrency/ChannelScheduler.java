package net.scaliby.marketaggregator.core.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class ChannelScheduler<K> {

    private final Executor executor;

    private final Map<K, ChannelRunner> runners = new ConcurrentHashMap<>();

    @Setter
    private int queueCapacity = 100_000;

    public void push(ChannelRunnable<K> job) {
        ChannelRunner channelRunner = runners.computeIfAbsent(job.getChannel(), k -> new ChannelRunner());
        channelRunner.push(job);
        if (!channelRunner.isRunning()) {
            executor.execute(channelRunner);
        }
    }

    @RequiredArgsConstructor
    private final class ChannelRunner implements Runnable {

        private final AtomicBoolean running = new AtomicBoolean(false);
        private final BlockingQueue<Runnable> workload = new ArrayBlockingQueue<>(queueCapacity);

        @Override
        public void run() {
            boolean oldValue = running.getAndSet(true);
            if (oldValue) {
                return;
            }
            while (hasData()) {
                Runnable elem = workload.poll();
                if (elem == null) {
                    break;
                }
                elem.run();
            }
            running.set(false);
            if (hasData()) {
                run();
            }
        }

        boolean isRunning() {
            return running.get();
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
