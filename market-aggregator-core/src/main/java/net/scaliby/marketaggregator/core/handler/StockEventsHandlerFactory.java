package net.scaliby.marketaggregator.core.handler;

public interface StockEventsHandlerFactory<K, V> {

    StockEventsHandler<K, V> create(K key);

}
