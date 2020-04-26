package net.scaliby.marketaggregator.core.market;

public interface Market {

    OrderBook getAsk();

    OrderBook getBid();

    Long getCurrentTime();

    StockEvent getLastEvent(String eventType);

}
