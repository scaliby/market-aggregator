package net.scaliby.marketaggregator.market;

public interface Market {

    OrderBook getAsk();

    OrderBook getBid();

    Long getCurrentTime();

    StockEvent getLastEvent(String eventType);

}
