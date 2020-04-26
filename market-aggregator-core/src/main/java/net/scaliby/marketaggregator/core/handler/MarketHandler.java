package net.scaliby.marketaggregator.core.handler;

import net.scaliby.marketaggregator.core.market.Market;

public interface MarketHandler<T> {

    T handle(Market market);

}
