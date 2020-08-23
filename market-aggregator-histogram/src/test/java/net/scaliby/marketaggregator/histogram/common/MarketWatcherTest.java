package net.scaliby.marketaggregator.histogram.common;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.Market;
import net.scaliby.marketaggregator.core.market.TestingMarketBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketWatcherTest {

    @Test
    public void askingForCompletion_returnsFalse_forWatcherTickedLessTimesThanSize() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 2);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(market);

        // then
        boolean isCompleted = marketWatcher.isCompleted();
        assertFalse(isCompleted);
    }

    @Test
    public void askingForCompletion_returnsTrue_forWatcherTickedTheSameAmountOfTimesAsSize() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 1);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(market);

        // then
        boolean isCompleted = marketWatcher.isCompleted();
        assertTrue(isCompleted);
    }

    @Test
    public void askingForCompletion_returnsTrue_forWatcherTickedMoreTimesThanSize() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 1);
        Market market = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(market);
        marketWatcher.tick(market);

        // then
        boolean isCompleted = marketWatcher.isCompleted();
        assertTrue(isCompleted);
    }

    @Test
    public void ticking_addsNewEntryToHistogram_forAsks() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 10);
        Market firstTickMarket = TestingMarketBuilder.builder()
                .build();
        Market secondTickMarket = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(firstTickMarket);
        marketWatcher.tick(secondTickMarket);

        // then
        double[][] expectedHistogram = new double[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        assertArrayEquals(expectedHistogram, marketWatcher.getAsksHistogram());
    }

    @Test
    public void ticking_addsNewEntryToHistogram_forBids() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 10);
        Market firstTickMarket = TestingMarketBuilder.builder()
                .build();
        Market secondTickMarket = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(firstTickMarket);
        marketWatcher.tick(secondTickMarket);

        // then
        double[][] expectedHistogram = new double[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        assertArrayEquals(expectedHistogram, marketWatcher.getBidsHistogram());
    }

    @Test
    public void tickingMoreTimesThanSize_returnsHistogramCutToSize_forAsks() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 1);
        Market firstTickMarket = TestingMarketBuilder.builder()
                .build();
        Market secondTickMarket = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(firstTickMarket);
        marketWatcher.tick(secondTickMarket);

        // then
        double[][] expectedHistogram = new double[][]{
                {0}
        };
        assertArrayEquals(expectedHistogram, marketWatcher.getAsksHistogram());
    }

    @Test
    public void tickingMoreTimesThanSize_returnsHistogramCutToSize_forBids() {
        // given
        MarketWatcher marketWatcher = new MarketWatcher("BTC", "USD", new DoubleWrapper(10), 1);
        Market firstTickMarket = TestingMarketBuilder.builder()
                .build();
        Market secondTickMarket = TestingMarketBuilder.builder()
                .build();

        // when
        marketWatcher.tick(firstTickMarket);
        marketWatcher.tick(secondTickMarket);

        // then
        double[][] expectedHistogram = new double[][]{
                {0}
        };
        assertArrayEquals(expectedHistogram, marketWatcher.getBidsHistogram());
    }

}