package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.DataHandlerFactory;

import java.io.PrintWriter;
import java.util.Optional;

public class AveragePricePrinterDataHandlerFactory implements DataHandlerFactory<Optional<Double>> {
    @Override
    public DataHandler<Optional<Double>> create() {
        PrintWriter printWriter = new PrintWriter(System.out);
        return new AveragePricePrinterDataHandler(printWriter);
    }
}
