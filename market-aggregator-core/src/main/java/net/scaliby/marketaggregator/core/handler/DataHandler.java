package net.scaliby.marketaggregator.core.handler;

import java.util.List;

public interface DataHandler<T> {

    void handle(List<T> data);

}
