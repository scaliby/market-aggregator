package net.scaliby.marketaggregator.core.common;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class LoggingTimer implements Timer {

    private final Clock clock;

    @Setter
    private long printInterval = TimeUnit.SECONDS.toMillis(5);

    private StockEvent newestStockEvent = null;
    private Instant lastLogTime = null;

    @Override
    public void handle(StockEvent stockEvent) {
        handle(Collections.singletonList(stockEvent));
    }

    @Override
    public void handle(List<StockEvent> events) {
        events.stream()
                .filter(event -> newestStockEvent == null || event.getTimestamp() > newestStockEvent.getTimestamp())
                .max(Comparator.comparing(StockEvent::getTimestamp))
                .ifPresent(this::handleEvent);
    }

    private void handleEvent(StockEvent stockEvent) {
        newestStockEvent = stockEvent;

        Instant now = clock.instant();
        if (lastLogTime == null || lastLogTime.plusMillis(printInterval).isBefore(now)) {
            printNewestStockEventTime();
            lastLogTime = now;
        }
    }

    private void printNewestStockEventTime() {
        log.info("Time: {}", newestStockEvent.getTimestamp());
    }

}
