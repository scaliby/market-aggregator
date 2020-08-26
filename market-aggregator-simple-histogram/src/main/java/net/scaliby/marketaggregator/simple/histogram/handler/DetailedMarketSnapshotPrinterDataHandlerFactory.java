package net.scaliby.marketaggregator.simple.histogram.handler;

import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.DataHandlerFactory;
import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;

import java.io.PrintWriter;

public class DetailedMarketSnapshotPrinterDataHandlerFactory<T> implements DataHandlerFactory<DetailedMarketSnapshot<T>> {
    @Override
    public DataHandler<DetailedMarketSnapshot<T>> create() {
        PrintWriter printWriter = new PrintWriter(System.out);
        return new DetailedMarketSnapshotPrinterDataHandler<>(printWriter);
    }
}
