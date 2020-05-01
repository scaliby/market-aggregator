package net.scaliby.marketaggregator.data.input;

import net.scaliby.marketaggregator.core.market.StockEvent;

public class PairStringKeyResolver implements KeyResolver<String> {
    @Override
    public String getKey(StockEvent stockEvent) {
        return stockEvent.getBase() + "_" + stockEvent.getQuote();
    }
}
