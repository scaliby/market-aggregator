package net.scaliby.marketaggregator.histogram.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;

import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class MarketSnapshotPrinterDataHandler implements DataHandler<MarketSnapshot> {

    private final PrintWriter printWriter;

    @Override
    public void handle(List<MarketSnapshot> data) {
        data.forEach(printWriter::println);
    }
}
