package net.scaliby.marketaggregator.market;

public interface MarketHandler<S, D> {

    D tick(Market<S> market);

}
