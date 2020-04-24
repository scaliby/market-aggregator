package net.scaliby.marketaggregator.market;

import net.scaliby.marketaggregator.common.DoubleWrapper;

public interface OrderBook {
    double getTotalAmountInBaseCurrency();

    double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step);
}
