package net.scaliby.marketaggregator.core.market;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CachedMutableOrderBook implements MutableOrderBook {
    private final MutableOrderBook delegate;

    private Map<CacheKey, double[]> marketDepthCache = new HashMap<>();
    private Double totalAmountInBaseCurrencyCache = null;

    @Override
    public void handle(DoubleWrapper price, Double amount) {
        marketDepthCache = new HashMap<>();
        totalAmountInBaseCurrencyCache = null;
        delegate.handle(price, amount);
    }

    @Override
    public double getTotalAmountInBaseCurrency() {
        if (totalAmountInBaseCurrencyCache == null) {
            totalAmountInBaseCurrencyCache = delegate.getTotalAmountInBaseCurrency();
        }
        return totalAmountInBaseCurrencyCache;
    }

    @Override
    public double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step) {
        CacheKey cacheKey = new CacheKey(samples, startingPrice, step);
        return marketDepthCache.computeIfAbsent(cacheKey, (key) -> delegate.getMarketDepth(samples, startingPrice, step));
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class CacheKey {
        private final int samples;
        private final DoubleWrapper startingPrice;
        private final DoubleWrapper step;
    }
}
