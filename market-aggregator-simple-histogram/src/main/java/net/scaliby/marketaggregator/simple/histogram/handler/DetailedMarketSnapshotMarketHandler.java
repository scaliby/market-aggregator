package net.scaliby.marketaggregator.simple.histogram.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.PriceSummary;
import net.scaliby.marketaggregator.core.handler.MarketHandler;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.OrderBook;
import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DetailedMarketSnapshotMarketHandler<K> implements MarketHandler<K, DetailedMarketSnapshot<K>> {

    private final int volumesSamples;

    private final List<Double> volumeHistory = new ArrayList<>();

    @Override
    public DetailedMarketSnapshot<K> handle(Market<K> market) {
        double marketVolume = getVolumeFromMarket(market);
        appendVolume(marketVolume);
        double cumulatedVolume = getCumulatedVolume();
        return DetailedMarketSnapshot.of(
                market.getKey(),
                market.getCurrentTime(),
                market.getPriceSummary().orElse(null),
                cumulatedVolume,
                getOrderBookSnapshot(market.getAsk(), cumulatedVolume),
                getOrderBookSnapshot(market.getBid(), cumulatedVolume)
        );
    }

    private DetailedMarketSnapshot.OrderBookSnapshot getOrderBookSnapshot(OrderBook orderBook, double depth) {
        return DetailedMarketSnapshot.OrderBookSnapshot.of(
                orderBook.getChangesCount(depth),
                orderBook.getChangesAmount(depth),
                orderBook.getOffers(depth)
        );
    }

    private double getCumulatedVolume() {
        return volumeHistory.stream()
                .mapToDouble(v -> v)
                .sum();
    }

    private double getVolumeFromMarket(Market<K> market) {
        return market.getPriceSummary()
                .flatMap(priceSummary -> Optional.ofNullable(priceSummary.getBuySell()))
                .map(PriceSummary.ByTypePriceSummary::getVolume)
                .orElse(0d);
    }

    private void appendVolume(double volume) {
        volumeHistory.add(volume);
        while (volumeHistory.size() > volumesSamples) {
            volumeHistory.remove(0);
        }
    }

}
