package net.scaliby.marketaggregator.histogram.handler;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.StockEvent;
import net.scaliby.marketaggregator.core.market.TestingMarketBuilder;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MarketSnapshotMarketHandlerTest {

    @Test
    public void handlingMarketWithoutTrades_returnsEmptyList_forHandlerEmittingOnlyOnTrade() {
        // given
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(1, true);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void handlingMarketWithTrade_returnsSingleSnapshot_forHandlerEmittingOnlyOnTrade() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(1, true);
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithoutTradesAfterInitializingWithTrade_returnsEmptyList_forHandlerEmittingOnlyOnTrade() {
        // given
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(2, true);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        handleTransaction(handler);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void handlingMarketWithoutTradesAfterInitializingWithTradeTwice_returnsSingleSnapshot_forHandlerEmittingNotOnlyOnTrade() {
        // given
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(2, false);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        handleTransaction(handler);
        handleTransaction(handler);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithTradeAfterInitializingWithTrade_returnsSingleSnapshot_forHandlerEmittingOnlyOnTrade() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(2, true);
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        handleTransaction(handler);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithoutTrades_returnsEmptyList_forHandlerEmittingNotOnlyOnTrade() {
        // given
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(1, false);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void handlingMarketWithTrade_returnsSingleSnapshot_forHandlerEmittingNotOnlyOnTrade() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(1, false);
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithoutTradesAfterInitializingWithTrade_returnsSingleSnapshot_forHandlerEmittingNotOnlyOnTrade() {
        // given
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(2, false);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        handleTransaction(handler);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithTradeAfterInitializingWithTrade_returnsSingleSnapshot_forHandlerEmittingNotOnlyOnTrade() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(2, false);
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        handleTransaction(handler);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void handlingMarketWithTradesCountLessThanSize_returnsEmptyList_forHandlerEmittingOnlyOnTrade() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        MarketSnapshotMarketHandler handler = new MarketSnapshotMarketHandler(10, true);
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        handler.handle(market);
        handler.handle(market);
        List<MarketSnapshot> result = handler.handle(market);

        // then
        assertEquals(0, result.size());
    }

    private void handleTransaction(MarketSnapshotMarketHandler handler) {
        StockEvent stockEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .build();
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(stockEvent)
                .build();
        handler.handle(market);
    }

}