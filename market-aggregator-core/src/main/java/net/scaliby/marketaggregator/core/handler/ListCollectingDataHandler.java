package net.scaliby.marketaggregator.core.handler;

import java.util.ArrayList;
import java.util.List;

public class ListCollectingDataHandler<T> implements DataHandler<T> {

    private final List<T> collectedData = new ArrayList<>();

    @Override
    public synchronized void handle(List<T> data) {
        collectedData.addAll(data);
    }

    public synchronized List<T> getCollectedData() {
        return new ArrayList<>(collectedData);
    }

}
