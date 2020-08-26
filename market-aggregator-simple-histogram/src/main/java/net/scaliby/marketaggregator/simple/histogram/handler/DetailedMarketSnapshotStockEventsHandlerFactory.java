package net.scaliby.marketaggregator.simple.histogram.handler;

import lombok.Setter;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;
import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;

public class DetailedMarketSnapshotStockEventsHandlerFactory<K> implements StockEventsHandlerFactory<K, DetailedMarketSnapshot<K>> {

    @Setter
    private final int volumeSamples = 240;

    @Override
    public StockEventsHandler<K, DetailedMarketSnapshot<K>> create(K key) {
        MarketAggregate<K> marketAggregate = MarketAggregateBuilder.<K>builder()
                .key(key)
                .build();
        return StockEventsHandlerBuilder.<K, DetailedMarketSnapshot<K>>builder()
                .marketAggregate(marketAggregate)
                .marketHandler(new DetailedMarketSnapshotMarketHandler<>(volumeSamples))
                .build();
    }
}
