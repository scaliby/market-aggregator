package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.Map;

public interface OrderBook {
    boolean isInBaseCurrency();

    double getTotalAmountInBaseCurrency();

    Map<DoubleWrapper, Integer> getChangesCount(double depth);

    Map<DoubleWrapper, Double> getChangesAmount(double depth);

    Map<DoubleWrapper, Double> getOffers(double depth);
}
