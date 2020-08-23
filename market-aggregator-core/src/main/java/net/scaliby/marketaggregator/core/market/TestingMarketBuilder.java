package net.scaliby.marketaggregator.core.market;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestingMarketBuilder {

    private double askTotalAmountInBaseCurrency = 10d;
    private double bidTotalAmountInBaseCurrency = 20d;
    private Long time = 1000L;
    private final Map<String, StockEvent> eventByType = new HashMap<>();

    private final Map<DoubleWrapper, Integer> askChangesCount = new HashMap<>();
    private final Map<DoubleWrapper, Double> askChangesAmount = new HashMap<>();
    private final Map<DoubleWrapper, Double> askOffers = new HashMap<>();

    private final Map<DoubleWrapper, Integer> bidChangesCount = new HashMap<>();
    private final Map<DoubleWrapper, Double> bidChangesAmount = new HashMap<>();
    private final Map<DoubleWrapper, Double> bidOffers = new HashMap<>();

    public static TestingMarketBuilder builder() {
        return new TestingMarketBuilder();
    }

    public TestingMarketBuilder askTotalAmountInBaseCurrency(double askTotalAmountInBaseCurrency) {
        this.askTotalAmountInBaseCurrency = askTotalAmountInBaseCurrency;
        return this;
    }

    public TestingMarketBuilder bidTotalAmountInBaseCurrency(double bidTotalAmountInBaseCurrency) {
        this.bidTotalAmountInBaseCurrency = bidTotalAmountInBaseCurrency;
        return this;
    }

    public TestingMarketBuilder time(Long time) {
        this.time = time;
        return this;
    }

    public TestingMarketBuilder withBidOffer(DoubleWrapper price, double amount, int changesCount, double changesAmount) {
        bidOffers.put(price, amount);
        bidChangesAmount.put(price, changesAmount);
        bidChangesCount.put(price, changesCount);
        return this;
    }

    public TestingMarketBuilder withAskOffer(DoubleWrapper price, double amount, int changesCount, double changesAmount) {
        askOffers.put(price, amount);
        askChangesAmount.put(price, changesAmount);
        askChangesCount.put(price, changesCount);
        return this;
    }

    public TestingMarketBuilder withStockEvent(StockEvent stockEvent) {
        eventByType.put(stockEvent.getType(), stockEvent);
        return this;
    }

    public Market build() {
        return new Market() {
            @Override
            public OrderBook getAsk() {
                return buildAskOrderBook();
            }

            @Override
            public OrderBook getBid() {
                return buildBidOrderBook();
            }

            @Override
            public Long getCurrentTime() {
                return time;
            }

            @Override
            public StockEvent getLastEvent(String eventType) {
                return eventByType.get(eventType);
            }
        };
    }

    private OrderBook buildBidOrderBook() {
        return new PredefinedOrderBook(bidChangesCount, bidChangesAmount, bidOffers, bidTotalAmountInBaseCurrency, true);
    }

    private OrderBook buildAskOrderBook() {
        return new PredefinedOrderBook(askChangesCount, askChangesAmount, askOffers, askTotalAmountInBaseCurrency, false);
    }

    @RequiredArgsConstructor
    private static class PredefinedOrderBook implements OrderBook {
        private final Map<DoubleWrapper, Integer> changesCount;
        private final Map<DoubleWrapper, Double> changesAmount;
        private final Map<DoubleWrapper, Double> offers;
        private final double totalAmountInBaseCurrency;
        private final boolean inBaseCurrency;

        @Override
        public boolean isInBaseCurrency() {
            return inBaseCurrency;
        }

        @Override
        public double getTotalAmountInBaseCurrency() {
            return totalAmountInBaseCurrency;
        }

        @Override
        public Map<DoubleWrapper, Integer> getChangesCount(double limitPrice) {
            return changesCount;
        }

        @Override
        public Map<DoubleWrapper, Double> getChangesAmount(double limitPrice) {
            return changesAmount;
        }

        @Override
        public Map<DoubleWrapper, Double> getOffers(double limitPrice) {
            return offers;
        }
    }
}
