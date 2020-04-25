package net.scaliby.marketaggregator.market;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.scaliby.marketaggregator.common.DoubleWrapper;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestingMarketBuilder {

    private double[] askMarketDepth = new double[]{1, 2};
    private double[] bidMarketDepth = new double[]{4, 5};
    private double askTotalAmountInBaseCurrency = 10d;
    private double bidTotalAmountInBaseCurrency = 20d;
    private Long time = 1000L;
    private Map<String, StockEvent> eventByType = new HashMap<>();

    public static TestingMarketBuilder builder() {
        return new TestingMarketBuilder();
    }

    public TestingMarketBuilder askMarketDepth(double[] askMarketDepth) {
        this.askMarketDepth = askMarketDepth;
        return this;
    }

    public TestingMarketBuilder bidMarketDepth(double[] bidMarketDepth) {
        this.bidMarketDepth = bidMarketDepth;
        return this;
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
        return new OrderBook() {
            @Override
            public double getTotalAmountInBaseCurrency() {
                return bidTotalAmountInBaseCurrency;
            }

            @Override
            public double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step) {
                return bidMarketDepth;
            }
        };
    }

    private OrderBook buildAskOrderBook() {
        return new OrderBook() {
            @Override
            public double getTotalAmountInBaseCurrency() {
                return askTotalAmountInBaseCurrency;
            }

            @Override
            public double[] getMarketDepth(int samples, DoubleWrapper startingPrice, DoubleWrapper step) {
                return askMarketDepth;
            }
        };
    }
}
