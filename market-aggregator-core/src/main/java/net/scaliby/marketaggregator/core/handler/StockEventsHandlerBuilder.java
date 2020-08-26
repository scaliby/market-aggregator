package net.scaliby.marketaggregator.core.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.MarketAggregateBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StockEventsHandlerBuilder<K, T> {

    private MarketAggregate<K> marketAggregate = MarketAggregateBuilder.<K>builder().build();
    private MarketHandler<K, T> marketHandler;

    public static <K, T> StockEventsHandlerBuilder<K, T> builder() {
        return new StockEventsHandlerBuilder<>();
    }

    public StockEventsHandlerBuilder<K, T> marketAggregate(MarketAggregate<K> aggregate) {
        this.marketAggregate = aggregate;
        return this;
    }

    public StockEventsHandlerBuilder<K, T> marketHandler(MarketHandler<K, T> handler) {
        this.marketHandler = handler;
        return this;
    }

    public StockEventsHandler<K, T> build() {
        return new StockEventsHandler<>(marketAggregate, marketHandler);
    }

}
