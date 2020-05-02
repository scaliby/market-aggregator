package net.scaliby.marketaggregator.core.handler;

public interface StockEventsHandlerFactory<V> {

    StockEventsHandler<V> create();

}
