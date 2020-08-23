package net.scaliby.marketaggregator.histogram.common;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.OrderBook;

import java.util.Map;
import java.util.TreeMap;

import static net.scaliby.marketaggregator.core.common.ComparatorCommon.getComparator;

@RequiredArgsConstructor
public class MarketDepthCalculator {

    private final OrderBook orderBook;

    public double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step) {
        Map<DoubleWrapper, Double> offers = orderBook.getOffers(samples * step.getValue());
        if (offers.isEmpty()) {
            return new double[samples];
        }

        TreeMap<DoubleWrapper, Double> data = new TreeMap<>(getComparator(orderBook.isInBaseCurrency()));
        data.putAll(offers);

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
        if (orderBook.isInBaseCurrency()) {
            return key >= currentPricePoint;
        }
        return key <= currentPricePoint;
    }

    private double getPricePoint(double step, int i, double startingPrice) {
        if (orderBook.isInBaseCurrency()) {
            return startingPrice - (step * i);
        }
        return startingPrice + (step * i);
    }

}
