package net.scaliby.marketaggregator.data.input;

import net.scaliby.marketaggregator.core.market.StockEvent;

public interface StockEventReader {

    StockEvent read();

}
