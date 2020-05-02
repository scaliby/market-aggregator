package net.scaliby.marketaggregator.histogram.handler;

import lombok.Setter;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerFactory;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;

import java.util.List;

public class MarketSnapshotStockEventsHandlerFactory implements StockEventsHandlerFactory<List<MarketSnapshot>> {

    @Setter
    private int size = 128;
    @Setter
    private boolean emitOnlyOnTrade = false;

    @Override
    public StockEventsHandler<List<MarketSnapshot>> create() {
        MarketAggregate marketAggregate = MarketAggregateBuilder.builder()
                .build();
        return StockEventsHandlerBuilder.<List<MarketSnapshot>>builder()
                .marketAggregate(marketAggregate)
                .marketHandler(new MarketSnapshotMarketHandler(size, emitOnlyOnTrade))
                .build();
    }
}
