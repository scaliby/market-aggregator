package net.scaliby.marketaggregator.simple.histogram.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;

import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class DetailedMarketSnapshotPrinterDataHandler<T> implements DataHandler<DetailedMarketSnapshot<T>> {

    private final PrintWriter printWriter;

    @Override
    public void handle(List<DetailedMarketSnapshot<T>> data) {
        data.forEach(printWriter::println);
    }
}
