package net.scaliby.marketaggregator.core.market;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CachedMutableOrderBook implements MutableOrderBook {
    private final MutableOrderBook delegate;

    private final Map<Double, Map<DoubleWrapper, Integer>> changesCountCache = new HashMap<>();
    private final Map<Double, Map<DoubleWrapper, Double>> changesAmountCache = new HashMap<>();
    private final Map<Double, Map<DoubleWrapper, Double>> offersCache = new HashMap<>();
    private Double totalAmountInBaseCurrencyCache = null;

    @Override
    public void handle(DoubleWrapper price, Double amount) {
        changesCountCache.clear();
        changesAmountCache.clear();
        offersCache.clear();
        totalAmountInBaseCurrencyCache = null;
        delegate.handle(price, amount);
    }

    @Override
    public void tick() {
        changesCountCache.clear();
        changesAmountCache.clear();
        delegate.tick();
    }

    @Override
    public boolean isInBaseCurrency() {
        return delegate.isInBaseCurrency();
    }

    @Override
    public double getTotalAmountInBaseCurrency() {
        if (totalAmountInBaseCurrencyCache == null) {
            totalAmountInBaseCurrencyCache = delegate.getTotalAmountInBaseCurrency();
        }
        return totalAmountInBaseCurrencyCache;
    }

    @Override
    public Map<DoubleWrapper, Integer> getChangesCount(double limitPrice) {
        return changesCountCache.computeIfAbsent(limitPrice, delegate::getChangesCount);
    }

    @Override
    public Map<DoubleWrapper, Double> getChangesAmount(double limitPrice) {
        return changesAmountCache.computeIfAbsent(limitPrice, delegate::getChangesAmount);
    }

    @Override
    public Map<DoubleWrapper, Double> getOffers(double limitPrice) {
        return offersCache.computeIfAbsent(limitPrice, delegate::getOffers);
    }
}
