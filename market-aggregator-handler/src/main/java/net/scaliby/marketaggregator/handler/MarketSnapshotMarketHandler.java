package net.scaliby.marketaggregator.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.handler.MarketHandler;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MarketSnapshotMarketHandler implements MarketHandler<List<MarketSnapshot>> {

    private final Integer size;
    private final boolean emitOnlyOnTrade;

    private List<MarketWatcher> watchers = new ArrayList<>();

    @Override
    public List<MarketSnapshot> handle(Market market) {
        addWatcherIfMarketHasTransaction(market);
        tickWatchers(market);
        List<MarketSnapshot> result = getSnapshots(market);
        cleanUpCompletedWatchersIfMarketHasTransaction(market);
        return result;
    }

    private void addWatcherIfMarketHasTransaction(Market market) {
        getLastBuySellEvent(market)
                .map(this::getWatcher)
                .ifPresent(watchers::add);
    }

    private MarketWatcher getWatcher(StockEvent stockEvent) {
        String base = stockEvent.getBase();
        String quote = stockEvent.getQuote();
        DoubleWrapper startingPrice = stockEvent.getPrice();
        return new MarketWatcher(base, quote, startingPrice, size);
    }

    private void tickWatchers(Market market) {
        watchers.forEach(watcher -> watcher.tick(market));
    }

    private void cleanUpCompletedWatchersIfMarketHasTransaction(Market market) {
        getLastBuySellEvent(market)
                .map(StockEvent::getPrice)
                .map(a -> getCompletedWatchers())
                .map(stream -> stream.collect(Collectors.toList()))
                .ifPresent(watchers::removeAll);
    }

    private List<MarketSnapshot> getSnapshots(Market market) {
        return getLastBuySellEvent(market)
                .map(StockEvent::getPrice)
                .map(price -> getSnapshotsForCompletedWatchers(market, price))
                .orElseGet(() -> getSnapshotsForCompletedWatchers(market))
                .filter(watcher -> watcher.getCurrentPrice() != null || !emitOnlyOnTrade)
                .collect(Collectors.toList());
    }

    private Stream<MarketSnapshot> getSnapshotsForCompletedWatchers(Market market, DoubleWrapper currentPrice) {
        return getCompletedWatchers()
                .map(watcher -> MarketSnapshot.of(
                        watcher.getBase(),
                        watcher.getQuote(),
                        watcher.getAsksHistogram(),
                        watcher.getBidsHistogram(),
                        market.getCurrentTime(),
                        watcher.getStartingPrice(),
                        currentPrice
                ));
    }

    private Stream<MarketSnapshot> getSnapshotsForCompletedWatchers(Market market) {
        return getCompletedWatchers()
                .map(watcher -> MarketSnapshot.of(
                        watcher.getBase(),
                        watcher.getQuote(),
                        watcher.getAsksHistogram(),
                        watcher.getBidsHistogram(),
                        market.getCurrentTime(),
                        watcher.getStartingPrice(),
                        null
                ));
    }

    private Stream<MarketWatcher> getCompletedWatchers() {
        return watchers.stream()
                .filter(MarketWatcher::isCompleted);
    }

    private Optional<StockEvent> getLastBuySellEvent(Market market) {
        return Stream.of("SELL", "BUY")
                .map(market::getLastEvent)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(StockEvent::getTimestamp));
    }
}
