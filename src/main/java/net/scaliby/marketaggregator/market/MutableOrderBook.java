package net.scaliby.marketaggregator.market;

import net.scaliby.marketaggregator.common.DoubleWrapper;

public interface MutableOrderBook extends OrderBook {
    void handle(DoubleWrapper price, Double amount);
}
