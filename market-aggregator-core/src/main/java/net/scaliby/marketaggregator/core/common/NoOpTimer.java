package net.scaliby.marketaggregator.core.common;

import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.List;

public class NoOpTimer implements Timer {
    @Override
    public void handle(StockEvent stockEvent) {
        // noop
    }

    @Override
    public void handle(List<StockEvent> events) {
        // noop
    }
}
