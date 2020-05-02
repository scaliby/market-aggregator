package net.scaliby.marketaggregator.core.input;

import net.scaliby.marketaggregator.core.market.StockEvent;

public class PairStringKeyResolver implements KeyResolver<String> {
    @Override
    public String getKey(StockEvent stockEvent) {
        return stockEvent.getBase() + "_" + stockEvent.getQuote();
    }
}
