package net.scaliby.marketaggregator.averageprice.handler;

import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.handler.DataHandler;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AveragePricePrinterDataHandler implements DataHandler<Optional<Double>> {

    private final PrintWriter printWriter;

    @Override
    public void handle(List<Optional<Double>> data) {
        data.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(printWriter::println);
    }
}
