package net.scaliby.marketaggregator.core.input;

import net.scaliby.marketaggregator.core.market.StockEvent;

public interface StockEventReader {

    StockEvent read();

}
