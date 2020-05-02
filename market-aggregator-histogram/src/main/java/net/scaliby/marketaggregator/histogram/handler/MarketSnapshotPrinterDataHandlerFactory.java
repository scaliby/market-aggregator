package net.scaliby.marketaggregator.histogram.handler;

import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.DataHandlerFactory;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;

import java.io.PrintWriter;

public class MarketSnapshotPrinterDataHandlerFactory implements DataHandlerFactory<MarketSnapshot> {
    @Override
    public DataHandler<MarketSnapshot> create() {
        PrintWriter printWriter = new PrintWriter(System.out);
        return new MarketSnapshotPrinterDataHandler(printWriter);
    }
}
