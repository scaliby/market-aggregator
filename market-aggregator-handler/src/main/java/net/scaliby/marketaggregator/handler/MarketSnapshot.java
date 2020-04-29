package net.scaliby.marketaggregator.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;

@Getter
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
