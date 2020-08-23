package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static net.scaliby.marketaggregator.core.common.ComparatorCommon.getComparator;

public class BasicMutableOrderBook implements MutableOrderBook {

    private final TreeMap<DoubleWrapper, Double> offers;
    private final TreeMap<DoubleWrapper, Double> changesAmount;
    private final TreeMap<DoubleWrapper, Integer> changesCount;
    private final boolean inverse;

    public BasicMutableOrderBook(boolean inverse) {
        this.inverse = inverse;
        this.offers = new TreeMap<>(getComparator(inverse));
        this.changesAmount = new TreeMap<>(getComparator(inverse));
        this.changesCount = new TreeMap<>(getComparator(inverse));
    }

    @Override
    public void handle(DoubleWrapper price, Double amount) {
        if (amount == null) {
            this.offers.remove(price);
        } else {
            this.changesAmount.merge(price, amount, Double::sum);
            this.offers.put(price, amount);
        }
        this.changesCount.merge(price, 1, Integer::sum);
    }

    @Override
    public void tick() {
        this.changesAmount.clear();
        this.changesCount.clear();
    }

    @Override
    public boolean isInBaseCurrency() {
        return inverse;
    }

    @Override
    public double getTotalAmountInBaseCurrency() {
        if (inverse) {
            return offers.values().stream()
                    .reduce(0D, Double::sum);
        }
        return offers.entrySet().stream()
                .map(entry -> entry.getKey().getValue() * entry.getValue())
                .reduce(0D, Double::sum);
    }

    @Override
    public Map<DoubleWrapper, Integer> getChangesCount(double depth) {
        return getHeadMap(changesCount, depth);
    }

    @Override
    public Map<DoubleWrapper, Double> getChangesAmount(double depth) {
        return getHeadMap(changesAmount, depth);
    }

    @Override
    public Map<DoubleWrapper, Double> getOffers(double depth) {
        return getHeadMap(offers, depth);
    }

    private <T> Map<DoubleWrapper, T> getHeadMap(TreeMap<DoubleWrapper, T> map, double depth) {
        if (map.isEmpty()) {
            return Collections.emptyMap();
        }
        double effectiveDepth = inverse ? -depth : depth;
        double limitPrice = map.firstKey().getValue() + effectiveDepth;
        NavigableMap<DoubleWrapper, T> head = map.headMap(new DoubleWrapper(limitPrice), true);
        return Collections.unmodifiableMap(head);
    }
}
