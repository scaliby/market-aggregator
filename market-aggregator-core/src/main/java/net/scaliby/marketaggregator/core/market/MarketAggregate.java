package net.scaliby.marketaggregator.core.market;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MarketAggregate implements Market {

    private final Integer timeWindow;
    private final MutableOrderBook ask;
    private final MutableOrderBook bid;

    @Getter
    private Long currentTime;

    private Map<String, StockEvent> lastEventByType = new HashMap<>();

    private static Map<String, StockEvent> getLastEventByTypeMap(List<StockEvent> stockEvents) {
        return stockEvents.stream()
                .collect(Collectors.toMap(StockEvent::getType, e -> e, (a, b) -> b));
    }

    private static Optional<Long> getMaxTime(List<StockEvent> stockEvents) {
        return stockEvents.stream()
                .map(StockEvent::getTimestamp)
                .max(Long::compareTo);
    }

    public boolean canApply(StockEvent stockEvent) {
        return canApply(Collections.singletonList(stockEvent));
    }

    public boolean canApply(List<StockEvent> stockEvents) {
        if (currentTime == null) {
            return true;
        }
        if (stockEvents.isEmpty()) {
            return true;
        }
        Long timeDiff = stockEvents.get(0).getTimestamp() - currentTime;
        return timeDiff < timeWindow;
    }

    public void apply(StockEvent stockEvent) {
        apply(Collections.singletonList(stockEvent));
    }

    public void apply(List<StockEvent> stockEvents) {
        getEventsByType(stockEvents, "ASK")
                .forEach(event -> ask.handle(event.getPrice(), event.getAmount()));

        getEventsByType(stockEvents, "BID")
                .forEach(event -> bid.handle(event.getPrice(), event.getAmount()));

        lastEventByType = getLastEventByTypeMap(stockEvents);

        if (currentTime == null) {
            getMaxTime(stockEvents)
                    .ifPresent(time -> currentTime = time);
        }
    }

    public void tick() {
        currentTime += timeWindow;
    }

    @Override
    public StockEvent getLastEvent(String eventType) {
        return lastEventByType.get(eventType);
    }

    @Override
    public OrderBook getAsk() {
        return ask;
    }

    @Override
    public OrderBook getBid() {
        return bid;
    }

    private Stream<StockEvent> getEventsByType(List<StockEvent> stockEvents, String type) {
        return stockEvents.stream()
                .filter(event -> event.getType().equals(type));
    }

}
