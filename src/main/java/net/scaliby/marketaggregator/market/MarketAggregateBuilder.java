package net.scaliby.marketaggregator.market;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarketAggregateBuilder {

    private int timeWindow = 1000;
    private boolean cachedOrderBook = true;

    public static MarketAggregateBuilder builder() {
        return new MarketAggregateBuilder();
    }

    public MarketAggregateBuilder timeWindow(int timeWindow) {
        this.timeWindow = timeWindow;
        return this;
    }

    public MarketAggregateBuilder cachedOrderBook(boolean cachedOrderBook) {
        this.cachedOrderBook = cachedOrderBook;
        return this;
    }

    public MarketAggregate build() {
        MutableOrderBook ask = new BasicMutableOrderBook(false);
        MutableOrderBook bid = new BasicMutableOrderBook(true);
        if (cachedOrderBook) {
            ask = new CachedMutableOrderBook(ask);
            bid = new CachedMutableOrderBook(bid);
        }
        return new MarketAggregate(timeWindow, ask, bid);
    }

}
