package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.common.PriceSummary;
import net.scaliby.marketaggregator.core.handler.MarketHandler;
import net.scaliby.marketaggregator.core.market.Market;

import java.util.Optional;

public class AveragePriceMarketHandler<K> implements MarketHandler<K, Optional<Double>> {
    @Override
    public Optional<Double> handle(Market<K> market) {
        return market.getPriceSummary()
                .map(PriceSummary::getBuySell)
                .map(PriceSummary.ByTypePriceSummary::getAvg);
    }
}
