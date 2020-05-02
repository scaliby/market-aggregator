package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;

import java.util.Optional;

public class AveragePriceStockEventsHandlerFactory implements StockEventsHandlerFactory<Optional<Double>> {
    @Override
    public StockEventsHandler<Optional<Double>> create() {
        MarketAggregate marketAggregate = MarketAggregateBuilder.builder()
                .build();
        return StockEventsHandlerBuilder.<Optional<Double>>builder()
                .marketAggregate(marketAggregate)
                .marketHandler(new AveragePriceMarketHandler())
                .build();
    }
}
