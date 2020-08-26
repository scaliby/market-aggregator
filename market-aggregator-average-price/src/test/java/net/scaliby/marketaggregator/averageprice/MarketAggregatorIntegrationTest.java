package net.scaliby.marketaggregator.averageprice;

import net.scaliby.marketaggregator.averageprice.handler.AveragePriceMarketHandler;
import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandlerBuilder;
import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarketAggregatorIntegrationTest {

    private final StockEventsHandler<String, Optional<Double>> stockEventsHandler = StockEventsHandlerBuilder.<String, Optional<Double>>builder()
            .marketHandler(new AveragePriceMarketHandler<>())
            .build();

    @Test
    public void handlingEventsForTheSameTimeWindow_returnsEmptyResultsArray() {
        // given
        StockEvent firstEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100D))
                .timestamp(1000L)
                .build();
        StockEvent secondEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(200D))
                .timestamp(1500L)
                .build();

        // when
        stockEventsHandler.handle(Collections.singletonList(firstEvent));
        List<Optional<Double>> results = stockEventsHandler.handle(Collections.singletonList(secondEvent));

        // then
        assertEquals(0, results.size());
    }

    @Test
    public void handlingEventsForNextTimeWindow_returnsAveragePriceForPreviousTimeWindow() {
        // given
        StockEvent buyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100D))
                .timestamp(1000L)
                .build();
        StockEvent sellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(200D))
                .timestamp(1000L)
                .build();
        StockEvent nextTimeWindowEvent = StockEvent.builder()
                .timestamp(2000L)
                .build();

        // when
        stockEventsHandler.handle(Arrays.asList(buyEvent, sellEvent));
        List<Optional<Double>> results = stockEventsHandler.handle(Collections.singletonList(nextTimeWindowEvent));

        // then
        assertEquals(1, results.size());
        Optional<Double> resultOptional = results.get(0);
        assertTrue(resultOptional.isPresent());
        assertEquals(150D, resultOptional.get(), 0.001);
    }

}
