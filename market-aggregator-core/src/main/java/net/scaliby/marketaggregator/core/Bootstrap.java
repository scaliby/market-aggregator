package net.scaliby.marketaggregator.core;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.Timer;
import net.scaliby.marketaggregator.core.concurrency.ChannelScheduler;
import net.scaliby.marketaggregator.core.handler.DataHandlerFactory;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.infrastructure.ChannelRunnableFactory;
import net.scaliby.marketaggregator.core.input.KeyResolver;
import net.scaliby.marketaggregator.core.input.PairTick;
import net.scaliby.marketaggregator.core.input.PairTickReader;
import net.scaliby.marketaggregator.core.input.StockEventReader;

import java.util.List;
import java.util.concurrent.Executor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Bootstrap<K, V> implements Runnable {

    private final Timer timer;
    private final KeyResolver<K> keyResolver;
    private final StockEventReader stockEventReader;
    private final Executor executor;
    private final StockEventsHandlerFactory<V> stockEventsHandlerFactory;
    private final DataHandlerFactory<V> dataHandlerFactory;

    @Override
    public void run() {
        PairTickReader<K> pairTickReader = new PairTickReader<>(stockEventReader, keyResolver);
        ChannelScheduler<K> channelScheduler = new ChannelScheduler<>(executor);
        ChannelRunnableFactory<K, V> channelRunnableFactory = new ChannelRunnableFactory<>(stockEventsHandlerFactory, dataHandlerFactory);
        while (true) {
            List<PairTick<K>> pairTicks = pairTickReader.read();
            if (pairTicks.isEmpty()) {
                break;
            }
            pairTicks.stream()
                    .map(PairTick::getEvents)
                    .forEach(timer::handle);
            pairTicks.stream()
                    .map(pairTick -> channelRunnableFactory.create(pairTick.getKey(), pairTick.getEvents()))
                    .forEach(channelScheduler::push);
        }
    }
}
