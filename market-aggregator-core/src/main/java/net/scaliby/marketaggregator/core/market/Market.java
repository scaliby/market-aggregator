package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.PriceSummary;

import java.util.Optional;

public interface Market<T> {

    T getKey();

    OrderBook getAsk();

    OrderBook getBid();

    Long getCurrentTime();

    StockEvent getLastEvent(String eventType);

    Optional<PriceSummary> getPriceSummary();

}
