package net.scaliby.marketaggregator.averageprice.handler;

import net.scaliby.marketaggregator.core.common.PriceSummary;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.TestingMarketBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AveragePriceMarketHandlerTest {

    private final AveragePriceMarketHandler handler = new AveragePriceMarketHandler();

    @Test
    public void handlingMarket_returnsEmptyOptional_forMarketWithoutPriceSummary() {
        // given
        Market market = TestingMarketBuilder.builder().build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void handlingMarket_returnsAveragePriceOfTwoEvents() {
        // given
        PriceSummary priceSummary = PriceSummary.builder()
                .buySell(PriceSummary.ByTypePriceSummary.builder().avg(15d).build())
                .build();
        Market market = TestingMarketBuilder.builder()
                .withPriceSummary(priceSummary)
                .build();

        // when
        Optional<Double> result = handler.handle(market);

        // then
        assertTrue(result.isPresent());
        assertEquals(15d, result.get(), 0.001);
    }

}