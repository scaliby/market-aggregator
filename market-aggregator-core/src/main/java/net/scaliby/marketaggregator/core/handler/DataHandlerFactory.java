package net.scaliby.marketaggregator.core.handler;

public interface DataHandlerFactory<T> {

    DataHandler<T> create();

}
