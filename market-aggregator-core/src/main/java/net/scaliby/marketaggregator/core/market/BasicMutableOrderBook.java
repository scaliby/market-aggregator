package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BasicMutableOrderBook implements MutableOrderBook {

    private final TreeMap<DoubleWrapper, Double> offers;
    private final boolean inverse;

    public BasicMutableOrderBook(boolean inverse) {
        this.inverse = inverse;
        if (inverse) {
            this.offers = new TreeMap<>(Comparator.reverseOrder());
        } else {
            this.offers = new TreeMap<>(Comparator.naturalOrder());
        }
    }

    @Override
    public void handle(DoubleWrapper price, Double amount) {
        if (amount == null) {
            this.offers.remove(price);
        } else {
            this.offers.put(price, amount);
        }
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
