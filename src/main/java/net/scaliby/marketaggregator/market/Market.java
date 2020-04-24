package net.scaliby.marketaggregator.market;

public interface Market<T> {

    OrderBook getAsk();

    OrderBook getBid();

    Long getCurrentTime();

    StockEvent getLastEvent(String eventType);

    T getData();

    void setData(T data);

}
