package net.scaliby.marketaggregator.simple.histogram.handler;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.common.PriceSummary;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.TestingMarketBuilder;
import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;
import org.junit.Test;

import static net.scaliby.marketaggregator.simple.histogram.common.MapCommon.newMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DetailedMarketSnapshotMarketHandlerTest {

    @Test
    public void handlingMarket_returnsSnapshotWithCorrectlyCumulatedVolume_forMarketThatCumulatedLessSamplesThanGiven() {
        // given
        DetailedMarketSnapshotMarketHandler<String> handler = new DetailedMarketSnapshotMarketHandler<>(1);
        Market<String> market = TestingMarketBuilder.<String>builder()
                .withPriceSummary(
                        PriceSummary.builder()
                                .buySell(
                                        PriceSummary.ByTypePriceSummary.builder()
                                                .volume(10d)
                                                .build()
                                )
                                .build()
                )
                .build();

        // when
        handler.handle(market);
        DetailedMarketSnapshot<String> result = handler.handle(market);

        // then
        assertEquals(10d, result.getCumulatedVolume(), 0.001d);
    }

    @Test
    public void handlingMarket_returnsSnapshotWithCumulatedVolumeEqualToZero_forMarketWithoutPriceSummary() {
        // given
        DetailedMarketSnapshotMarketHandler<String> handler = new DetailedMarketSnapshotMarketHandler<>(1);
        Market<String> market = TestingMarketBuilder.<String>builder()
                .build();

        // when
        handler.handle(market);
        DetailedMarketSnapshot<String> result = handler.handle(market);

        // then
        assertEquals(0d, result.getCumulatedVolume(), 0.001d);
    }

    @Test
    public void handlingMarket_returnsSnapshotWithoutPriceSummary_forMarketWithoutPriceSummary() {
        // given
        DetailedMarketSnapshotMarketHandler<String> handler = new DetailedMarketSnapshotMarketHandler<>(1);
        Market<String> market = TestingMarketBuilder.<String>builder()
                .build();

        // when
        DetailedMarketSnapshot<String> result = handler.handle(market);

        // then
        assertNull(result.getPriceSummary());
    }

    @Test
    public void handlingMarket_returnsValidSnapshot_forMarketWithKeyTimePriceSummaryAndOffers() {
        // given
        DetailedMarketSnapshotMarketHandler<String> handler = new DetailedMarketSnapshotMarketHandler<>(1);
        PriceSummary priceSummary = PriceSummary.builder()
                .buySell(
                        PriceSummary.ByTypePriceSummary.builder()
                                .volume(10d)
                                .build()
                )
                .build();
        Market<String> market = TestingMarketBuilder.<String>builder()
                .key("BTC_USD")
                .time(1000L)
                .withPriceSummary(priceSummary)
                .withAskOffer(new DoubleWrapper(10), 10d, 1, 10d)
                .withBidOffer(new DoubleWrapper(20), 20d, 2, 20d)
                .build();

        // when
        DetailedMarketSnapshot<String> result = handler.handle(market);

        // then
        DetailedMarketSnapshot<String> expectedResult = DetailedMarketSnapshot.<String>builder()
                .key("BTC_USD")
                .currentTime(1000L)
                .cumulatedVolume(10d)
                .priceSummary(priceSummary)
                .ask(
                        DetailedMarketSnapshot.OrderBookSnapshot.builder()
                                .offers(newMap(new DoubleWrapper(10d), 10d))
                                .changesAmount(newMap(new DoubleWrapper(10d), 10d))
                                .changesCount(newMap(new DoubleWrapper(10d), 1))
                                .build()
                )
                .bid(
                        DetailedMarketSnapshot.OrderBookSnapshot.builder()
                                .offers(newMap(new DoubleWrapper(20d), 20d))
                                .changesAmount(newMap(new DoubleWrapper(20d), 20d))
                                .changesCount(newMap(new DoubleWrapper(20d), 2))
                                .build()
                )
                .build();
        assertEquals(expectedResult, result);
    }

}