package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.*;

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

    private static Comparator<DoubleWrapper> getComparator(boolean inverse) {
        if (inverse) {
            return Comparator.reverseOrder();
        }
        return Comparator.naturalOrder();
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

    @Override
    public double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step) {
        double toKey = getPricePoint(step.getValue(), samples - 1, startingPrice.getValue());
        NavigableMap<DoubleWrapper, Double> data = new TreeMap<>(offers.headMap(new DoubleWrapper(toKey), true));

        Map.Entry<DoubleWrapper, Double> firstEntry;
        Double currentAmount = 0D;
        double[] result = new double[samples];
        for (int i = 0; i < samples; i++) {
            double currentPricePoint = getPricePoint(step.getValue(), i, startingPrice.getValue());
            while (!data.isEmpty() && shouldProceedToNextEntry(data.firstEntry().getKey().getValue(), currentPricePoint)) {
                firstEntry = data.pollFirstEntry();
                currentAmount += firstEntry.getValue();
            }
            result[i] = currentAmount;
        }
        return result;
    }

    private boolean shouldProceedToNextEntry(double key, double currentPricePoint) {
        if (inverse) {
            return key >= currentPricePoint;
        }
        return key <= currentPricePoint;
    }

    private double getPricePoint(double step, int i, double startingPrice) {
        if (inverse) {
            return startingPrice - (step * i);
        }
        return startingPrice + (step * i);
    }

}
