package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

public interface MutableOrderBook extends OrderBook {
    void handle(DoubleWrapper price, Double amount);
}
