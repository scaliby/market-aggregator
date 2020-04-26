package net.scaliby.marketaggregator.core.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class StockEventsHandler<T> {

    private final MarketAggregate aggregate;
    private final MarketHandler<T> marketHandler;

    public List<T> handle(StockEvent stockEvent) {
        return handle(Collections.singletonList(stockEvent));
    }

    public List<T> handle(List<StockEvent> events) {
        List<T> result = new ArrayList<>();
        while (!aggregate.canApply(events)) {
            aggregate.tick();
            result.add(marketHandler.handle(aggregate));
        }
        aggregate.apply(events);
        return result;
    }

}
