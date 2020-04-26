package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

public interface OrderBook {
    double getTotalAmountInBaseCurrency();

    double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step);
}
