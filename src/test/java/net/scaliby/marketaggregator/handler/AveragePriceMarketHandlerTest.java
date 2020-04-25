package net.scaliby.marketaggregator.handler;

import net.scaliby.marketaggregator.common.DoubleWrapper;
import net.scaliby.marketaggregator.market.Market;
import net.scaliby.marketaggregator.market.StockEvent;
import net.scaliby.marketaggregator.market.TestingMarketBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AveragePriceMarketHandlerTest {

    private AveragePriceMarketHandler handler = new AveragePriceMarketHandler();

    @Test
    public void handlingMarket_returnsEmptyOptional_forMarketWithoutStockEvents() {
        // given
        Market market = TestingMarketBuilder.builder().build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void handlingMarket_returnsPriceOfSingleEvent_forMarketWithBuyStockEvent() {
        // given
        double expectedResult = 100d;

        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(expectedResult))
                .build();
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get(), 0.001);
    }

    @Test
    public void handlingMarket_returnsPriceOfSingleEvent_forMarketWithSellStockEvent() {
        // given
        double expectedResult = 100d;

        StockEvent sell = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(expectedResult))
                .build();
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(sell)
                .build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get(), 0.001);
    }

    @Test
    public void handlingMarket_returnsAveragePriceOfTwoEvents_forMarketWithBuyAndSellStockEvent() {
        // given
        StockEvent buy = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10d))
                .build();
        StockEvent sell = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(20d))
                .build();
        Market market = TestingMarketBuilder.builder()
                .withStockEvent(buy)
                .withStockEvent(sell)
                .build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertTrue(result.isPresent());
        assertEquals(15d, result.get(), 0.001);
    }

}