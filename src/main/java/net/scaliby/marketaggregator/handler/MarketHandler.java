package net.scaliby.marketaggregator.handler;

import net.scaliby.marketaggregator.market.Market;

public interface MarketHandler<T> {

    T handle(Market market);

}
