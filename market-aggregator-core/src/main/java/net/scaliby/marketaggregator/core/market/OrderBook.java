package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.Map;

public interface OrderBook {
    boolean isInBaseCurrency();

    double getTotalAmountInBaseCurrency();

    Map<DoubleWrapper, Integer> getChangesCount(double depth);

    Map<DoubleWrapper, Double> getChangesAmount(double depth);

    Map<DoubleWrapper, Double> getOffers(double depth);

    /**
     * @deprecated Use getOffers(double depth) instead and calculate market depth on your own
     */
    @Deprecated
    double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step);
}
