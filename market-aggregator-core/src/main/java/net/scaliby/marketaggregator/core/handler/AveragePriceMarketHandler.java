package net.scaliby.marketaggregator.core.handler;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AveragePriceMarketHandler implements MarketHandler<Optional<Double>> {
    @Override
    public Optional<Double> handle(Market market) {
        List<StockEvent> events = getBuySellEvents(market);
        if (events.size() == 0) {
            return Optional.empty();
        }
        double avg = sum(events) / events.size();
        return Optional.of(avg);
    }

    private List<StockEvent> getBuySellEvents(Market market) {
        return Stream.of("BUY", "SELL")
                .map(market::getLastEvent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private double sum(List<StockEvent> events) {
        return events.stream()
                .map(StockEvent::getPrice)
                .mapToDouble(DoubleWrapper::getValue)
                .reduce(0, Double::sum);
    }
}
