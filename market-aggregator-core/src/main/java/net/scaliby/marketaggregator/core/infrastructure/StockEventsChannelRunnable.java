package net.scaliby.marketaggregator.core.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.concurrency.ChannelRunnable;
import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.List;

@RequiredArgsConstructor
public class StockEventsChannelRunnable<K, T> implements ChannelRunnable<K> {

    @Getter
    private final K channel;
    private final List<StockEvent> events;
    private final StockEventsHandler<T> eventsHandler;
    private final DataHandler<T> dataHandler;

    @Override
    public void run() {
        List<T> result = eventsHandler.handle(events);
        dataHandler.handle(result);
    }
}
