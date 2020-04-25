package net.scaliby.marketaggregator.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.scaliby.marketaggregator.market.MarketAggregate;
import net.scaliby.marketaggregator.market.MarketAggregateBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StockEventsHandlerBuilder<T> {

    private MarketAggregate marketAggregate = MarketAggregateBuilder.builder().build();
    private MarketHandler<T> marketHandler;

    public static <T> StockEventsHandlerBuilder<T> builder() {
        return new StockEventsHandlerBuilder<T>();
    }

    public StockEventsHandlerBuilder<T> marketAggregate(MarketAggregate aggregate) {
        this.marketAggregate = aggregate;
        return this;
    }

    public StockEventsHandlerBuilder<T> marketHandler(MarketHandler<T> handler) {
        this.marketHandler = handler;
        return this;
    }

    public StockEventsHandler<T> build() {
        return new StockEventsHandler<>(marketAggregate, marketHandler);
    }

}
