package net.scaliby.marketaggregator.core.handler;

import net.scaliby.marketaggregator.core.market.Market;

public interface MarketHandler<K, T> {

    T handle(Market<K> market);

}
