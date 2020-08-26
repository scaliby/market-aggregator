package net.scaliby.marketaggregator.histogram.handler;

import lombok.Setter;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;

import java.util.List;

public class MarketSnapshotStockEventsHandlerFactory<K> implements StockEventsHandlerFactory<K, List<MarketSnapshot>> {

    @Setter
    private int size = 128;
    @Setter
    private boolean emitOnlyOnTrade = false;

    @Override
    public StockEventsHandler<K, List<MarketSnapshot>> create(K key) {
        MarketAggregate<K> marketAggregate = MarketAggregateBuilder.<K>builder()
                .key(key)
                .build();
        return StockEventsHandlerBuilder.<K, List<MarketSnapshot>>builder()
                .marketAggregate(marketAggregate)
                .marketHandler(new MarketSnapshotMarketHandler<>(size, emitOnlyOnTrade))
                .build();
    }
}
