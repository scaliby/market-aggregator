package net.scaliby.marketaggregator.core.input;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListStockEventReader implements StockEventReader {

    private final List<StockEvent> events;
    private int index = 0;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public StockEvent read() {
        if (events.size() <= index) {
            return null;
        }

        StockEvent event = events.get(index);
        index++;
        return event;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final List<StockEvent> events = new ArrayList<>();

        public Builder withStockEvent(StockEvent stockEvent) {
            events.add(stockEvent);
            return this;
        }

        public ListStockEventReader build() {
            return new ListStockEventReader(events);
        }
    }

}
