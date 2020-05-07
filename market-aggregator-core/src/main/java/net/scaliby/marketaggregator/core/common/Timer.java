package net.scaliby.marketaggregator.core.common;

import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.List;

public interface Timer {
    static Timer noOp() {
        return new NoOpTimer();
    }

    void handle(StockEvent stockEvent);

    void handle(List<StockEvent> events);
}
