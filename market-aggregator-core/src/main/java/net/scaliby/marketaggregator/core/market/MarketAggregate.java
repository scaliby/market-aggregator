package net.scaliby.marketaggregator.core.market;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.common.PriceSummary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MarketAggregate<T> implements Market<T> {

    @Getter
    private final T key;
    private final Integer timeWindow;
    private final MutableOrderBook ask;
    private final MutableOrderBook bid;

    @Getter
    private Long currentTime;

    private Map<String, StockEvent> lastEventByType = new HashMap<>();

    private final List<StockEvent> buySellEvents = new ArrayList<>();

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
        long timeDiff = stockEvents.get(0).getTimestamp() - currentTime;
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

        List<StockEvent> newBuySellEvents = stockEvents.stream()
                .filter(event -> event.getType().equals("BUY") || event.getType().equals("SELL"))
                .collect(Collectors.toList());
        buySellEvents.addAll(newBuySellEvents);

        lastEventByType = getLastEventByTypeMap(stockEvents);

        if (currentTime == null) {
            getMaxTime(stockEvents)
                    .ifPresent(time -> currentTime = time);
        }
    }

    public void tick() {
        currentTime += timeWindow;
        ask.tick();
        bid.tick();
        buySellEvents.clear();
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

    @Override
    public Optional<PriceSummary> getPriceSummary() {
        if (buySellEvents.isEmpty()) {
            return Optional.empty();
        }
        double open = buySellEvents.get(0).getPrice().getValue();
        double close = buySellEvents.get(buySellEvents.size() - 1).getPrice().getValue();
        List<StockEvent> buyEvents = getEventsByType(buySellEvents, "BUY")
                .collect(Collectors.toList());
        List<StockEvent> sellEvents = getEventsByType(buySellEvents, "SELL")
                .collect(Collectors.toList());
        PriceSummary.ByTypePriceSummary buyPriceSummary = getByTypePriceSummaryOrNull(buyEvents);
        PriceSummary.ByTypePriceSummary sellPriceSummary = getByTypePriceSummaryOrNull(sellEvents);
        PriceSummary.ByTypePriceSummary buySellPriceSummary = getByTypePriceSummaryOrNull(buySellEvents);
        return Optional.of(
                PriceSummary.of(
                        open,
                        close,
                        buyEvents.size(),
                        sellEvents.size(),
                        buySellPriceSummary,
                        buyPriceSummary,
                        sellPriceSummary
                )
        );
    }

    private PriceSummary.ByTypePriceSummary getByTypePriceSummaryOrNull(List<StockEvent> events) {
        if (events.isEmpty()) {
            return null;
        }
        double min = events.stream()
                .map(StockEvent::getPrice)
                .mapToDouble(DoubleWrapper::getValue)
                .min()
                .orElseThrow(() -> new RuntimeException("Empty events"));
        double max = events.stream()
                .map(StockEvent::getPrice)
                .mapToDouble(DoubleWrapper::getValue)
                .max()
                .orElseThrow(() -> new RuntimeException("Empty events"));
        double volume = events.stream()
                .mapToDouble(StockEvent::getAmount)
                .sum();
        double priceSum = events.stream()
                .map(StockEvent::getPrice)
                .mapToDouble(DoubleWrapper::getValue)
                .sum();
        double avg = priceSum / events.size();
        double weightedAvg = events.stream()
                .mapToDouble(event -> event.getPrice().getValue() * event.getAmount())
                .sum() / events.size();
        return PriceSummary.ByTypePriceSummary.of(min, max, avg, weightedAvg, volume);
    }

    private Stream<StockEvent> getEventsByType(List<StockEvent> stockEvents, String type) {
        return stockEvents.stream()
                .filter(event -> event.getType().equals(type));
    }

}
