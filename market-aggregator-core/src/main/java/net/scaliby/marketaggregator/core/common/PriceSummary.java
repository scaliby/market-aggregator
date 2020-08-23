package net.scaliby.marketaggregator.core.common;

import lombok.*;

@Getter
@AllArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode
@Builder
public class PriceSummary {
    @Builder.Default
    private final double open = 10d;
    @Builder.Default
    private final double close = 20d;
    @Builder.Default
    private final int buyTransactionCount = 2;
    @Builder.Default
    private final int sellTransactionCount = 3;
    @Builder.Default
    private final ByTypePriceSummary buySell = ByTypePriceSummary.of(10d, 20d, 15d, 15d, 2d);
    @Builder.Default
    private final ByTypePriceSummary buy = ByTypePriceSummary.of(10d, 20d, 15d, 15d, 2d);
    @Builder.Default
    private final ByTypePriceSummary sell = ByTypePriceSummary.of(10d, 20d, 15d, 15d, 2d);

    @Getter
    @AllArgsConstructor(staticName = "of")
    @ToString
    @EqualsAndHashCode
    @Builder
    public static class ByTypePriceSummary {
        @Builder.Default
        private final double min = 10d;
        @Builder.Default
        private final double max = 20d;
        @Builder.Default
        private final double avg = 15d;
        @Builder.Default
        private final double weightedAvg = 15d;
        @Builder.Default
        private final double volume = 2d;
    }

}
