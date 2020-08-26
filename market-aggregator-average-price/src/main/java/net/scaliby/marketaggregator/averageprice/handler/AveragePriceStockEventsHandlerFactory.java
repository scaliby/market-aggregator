package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;

import java.util.Optional;

public class AveragePriceStockEventsHandlerFactory<K> implements StockEventsHandlerFactory<K, Optional<Double>> {
    @Override
    public StockEventsHandler<K, Optional<Double>> create(K key) {
        MarketAggregate<K> marketAggregate = MarketAggregateBuilder.<K>builder()
                .key(key)
                .build();
        return StockEventsHandlerBuilder.<K, Optional<Double>>builder()
                .marketAggregate(marketAggregate)
                .marketHandler(new AveragePriceMarketHandler<>())
                .build();
    }
}
