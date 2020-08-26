package net.scaliby.marketaggregator.simple.histogram.common;

import lombok.*;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.common.PriceSummary;

import java.util.Map;

import static net.scaliby.marketaggregator.simple.histogram.common.MapCommon.newMap;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@Builder
public class DetailedMarketSnapshot<T> {
    private final T key;
    @Builder.Default
    private final Long currentTime = 1000L;
    @Builder.Default
    private final PriceSummary priceSummary = PriceSummary.builder().build();
    @Builder.Default
    private final double cumulatedVolume = 10d;
    @Builder.Default
    private final OrderBookSnapshot ask = OrderBookSnapshot.builder().build();
    @Builder.Default
    private final OrderBookSnapshot bid = OrderBookSnapshot.builder().build();

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor(staticName = "of")
    @Builder
    public static class OrderBookSnapshot {
        @Builder.Default
        private final Map<DoubleWrapper, Integer> changesCount = newMap(new DoubleWrapper(10d), 10);
        @Builder.Default
        private final Map<DoubleWrapper, Double> changesAmount = newMap(new DoubleWrapper(20d), 20d);
        @Builder.Default
        private final Map<DoubleWrapper, Double> offers = newMap(new DoubleWrapper(30d), 30d);
    }
}
