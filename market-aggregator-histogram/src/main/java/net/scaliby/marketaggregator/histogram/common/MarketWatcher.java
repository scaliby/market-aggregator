package net.scaliby.marketaggregator.histogram.common;

import lombok.Getter;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.Market;

import java.util.ArrayList;
import java.util.List;

public class MarketWatcher {

    @Getter
    private final String base;
    @Getter
    private final String quote;
    @Getter
    private final DoubleWrapper startingPrice;
    private final int size;
    private final List<double[]> asks;
    private final List<double[]> bids;

    public MarketWatcher(String base, String quote, DoubleWrapper startingPrice, int size) {
        this.base = base;
        this.quote = quote;
        this.startingPrice = startingPrice;
        this.size = size;
        this.asks = new ArrayList<>(size);
        this.bids = new ArrayList<>(size);
    }

    public void tick(Market market) {
        MarketDepthCalculator askCalculator = new MarketDepthCalculator(market.getAsk());
        MarketDepthCalculator bidCalculator = new MarketDepthCalculator(market.getBid());
        DoubleWrapper step = DoubleWrapper.ONE;
        double[] ask = askCalculator.getMarketDepth(size, startingPrice, step);
        double[] bid = bidCalculator.getMarketDepth(size, startingPrice, step);
        asks.add(ask);
        bids.add(bid);
        while (asks.size() > size) {
            asks.remove(0);
        }
        while (bids.size() > size) {
            bids.remove(0);
        }
    }

    public boolean isCompleted() {
        return asks.size() == size && bids.size() == size;
    }

    public double[][] getAsksHistogram() {
        return toArray(asks);
    }

    public double[][] getBidsHistogram() {
        return toArray(bids);
    }

    private double[][] toArray(List<double[]> input) {
        double[][] result = new double[input.size()][];
        input.toArray(result);
        return result;
    }

}
