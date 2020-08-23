package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.common.PriceSummary;
import net.scaliby.marketaggregator.core.handler.MarketHandler;
import net.scaliby.marketaggregator.core.market.Market;

import java.util.Optional;

public class AveragePriceMarketHandler implements MarketHandler<Optional<Double>> {
    @Override
    public Optional<Double> handle(Market market) {
        return market.getPriceSummary()
                .map(PriceSummary::getBuySell)
                .map(PriceSummary.ByTypePriceSummary::getAvg);
    }
}
