package net.scaliby.marketaggregator.core.infrastructure;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.concurrency.ChannelRunnable;
import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.DataHandlerFactory;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ChannelRunnableFactory<K, V> {

    private final StockEventsHandlerFactory<V> stockEventsHandlerFactory;
    private final DataHandlerFactory<V> dataHandlerFactory;

    private final Map<K, StockEventsHandler<V>> stockEventsHandlers = new HashMap<>();

    public ChannelRunnable<K> create(K key, List<StockEvent> events) {
        StockEventsHandler<V> stockEventsHandler = stockEventsHandlers.computeIfAbsent(
                key,
                k -> stockEventsHandlerFactory.create()
        );
        DataHandler<V> dataHandler = dataHandlerFactory.create();
        return new StockEventsChannelRunnable<>(key, events, stockEventsHandler, dataHandler);
    }

}
