package net.scaliby.marketaggregator.histogram.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class MarketSnapshot {

    private final String base;
    private final String quote;
    private final double[][] asks;
    private final double[][] bids;
    private final Long currentTime;
    private final DoubleWrapper originalPrice;
    private final DoubleWrapper currentPrice;

}
