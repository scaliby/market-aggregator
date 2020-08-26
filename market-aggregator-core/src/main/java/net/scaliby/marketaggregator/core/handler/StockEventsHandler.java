package net.scaliby.marketaggregator.core.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StockEventsHandler<K, T> {

    private final MarketAggregate<K> aggregate;
    private final MarketHandler<K, T> marketHandler;

    public List<T> handle(List<StockEvent> events) {
        List<T> result = new ArrayList<>();
        while (!aggregate.canApply(events)) {
            result.add(marketHandler.handle(aggregate));
            aggregate.tick();
        }
        aggregate.apply(events);
        return result;
    }

}
