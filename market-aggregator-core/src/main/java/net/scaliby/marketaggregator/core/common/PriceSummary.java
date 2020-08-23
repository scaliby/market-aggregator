package net.scaliby.marketaggregator.core.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode
public class PriceSummary {

    private final double open;
    private final double close;
    private final int buyTransactionCount;
    private final int sellTransactionCount;
    private final ByTypePriceSummary buy;
    private final ByTypePriceSummary sell;

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    @ToString
    @EqualsAndHashCode
    public static class ByTypePriceSummary {
        private final double min;
        private final double max;
        private final double avg;
        private final double weightedAvg;
        private final double volume;
    }

}
