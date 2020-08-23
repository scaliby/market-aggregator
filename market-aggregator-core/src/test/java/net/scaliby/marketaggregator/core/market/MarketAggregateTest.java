package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.common.PriceSummary;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class MarketAggregateTest {

    private final MarketAggregate marketAggregate = new MarketAggregate(
            1000,
            new BasicMutableOrderBook(false),
            new BasicMutableOrderBook(true)
    );

    @Test
    public void askingIfCanApplyEvent_returnsTrue_forNewlyCreatedAggregate() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .timestamp(1000L)
                .build();

        // when
        boolean result = marketAggregate.canApply(stockEvent);

        // then
        assertTrue(result);
    }

    @Test
    public void askingIfAggregateCanApplyEmptyEventsList_returnsTrue_forAggregateAtSomeTimeState() {
        // given
        StockEvent existingEvent = StockEvent.builder()
                .timestamp(1000L)
                .build();

        // when
        marketAggregate.apply(existingEvent);
        boolean result = marketAggregate.canApply(Collections.emptyList());

        // then
        assertTrue(result);
    }

    @Test
    public void askingIfAggregateCanApplyEvent_returnsFalse_forEventThatIsNewerThanCurrentAggregateTimeWindow() {
        // given
        StockEvent initialStockEvent = StockEvent.builder()
                .timestamp(1000L)
                .build();
        StockEvent newStockEvent = StockEvent.builder()
                .timestamp(2500L)
                .build();

        // when
        marketAggregate.apply(initialStockEvent);
        boolean result = marketAggregate.canApply(newStockEvent);

        // then
        assertFalse(result);
    }

    @Test
    public void askingIfAggregateCanApplyEventAfterTicking_returnTrue_forEventThatIsFittingIntoNextAggregateTimeWindow() {
        // given
        StockEvent initialStockEvent = StockEvent.builder()
                .timestamp(1000L)
                .build();
        StockEvent newStockEvent = StockEvent.builder()
                .timestamp(2500L)
                .build();

        // when
        marketAggregate.apply(initialStockEvent);
        marketAggregate.tick();
        boolean result = marketAggregate.canApply(newStockEvent);

        // then
        assertTrue(result);
    }

    @Test
    public void askingIfAggregateCanApplyEvent_returnsTrue_forEventThatIsFittingIntoCurrentAggregateTimeWindow() {
        // given
        StockEvent initialStockEvent = StockEvent.builder()
                .timestamp(1000L)
                .build();
        StockEvent newStockEvent = StockEvent.builder()
                .timestamp(1500L)
                .build();

        // when
        marketAggregate.apply(initialStockEvent);
        boolean result = marketAggregate.canApply(newStockEvent);

        // then
        assertTrue(result);
    }

    @Test
    public void applyingStockEventList_makesGetLastEventReturnLastEventFromList() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .type("ASK")
                .sequence(1L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .type("ASK")
                .sequence(2L)
                .build();

        // when
        marketAggregate.apply(Arrays.asList(firstStockEvent, secondStockEvent));

        // then
        StockEvent result = marketAggregate.getLastEvent("ASK");
        assertEquals(secondStockEvent, result);
    }

    @Test
    public void applyingStockEvent_makesAskOrderBookReturnValidTotalAmountInBaseCurrency_forASKEvent() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .type("ASK")
                .price(new DoubleWrapper(10))
                .amount(10D)
                .build();

        // when
        marketAggregate.apply(stockEvent);

        // then
        double result = marketAggregate.getAsk().getTotalAmountInBaseCurrency();
        assertEquals(100, result, 0.001);
    }

    @Test
    public void applyingStockEvent_makesAskOrderBookReturnValidTotalAmountInBaseCurrency_forBIDEvent() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .type("BID")
                .price(new DoubleWrapper(10))
                .amount(10D)
                .build();

        // when
        marketAggregate.apply(stockEvent);

        // then
        double result = marketAggregate.getBid().getTotalAmountInBaseCurrency();
        assertEquals(10, result, 0.001);
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetTimeReturnTimeOfLastEventFromBatch_forSingleBatch() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .timestamp(100L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .timestamp(200L)
                .build();

        // when
        marketAggregate.apply(Arrays.asList(firstStockEvent, secondStockEvent));

        // then
        long currentTime = marketAggregate.getCurrentTime();
        assertEquals(currentTime, 200L);
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetTimeReturnTimeOfLastEventFromFirstBatch_forMultipleBatches() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .timestamp(100L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .timestamp(200L)
                .build();

        // when
        marketAggregate.apply(firstStockEvent);
        marketAggregate.apply(secondStockEvent);

        // then
        long currentTime = marketAggregate.getCurrentTime();
        assertEquals(currentTime, 100L);
    }

    @Test
    public void tickingAggregateAfterApplyingSeriesOfStockEvents_makesGetPriceSummaryReturnEmptyOptional() {
        // given
        StockEvent buyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100))
                .build();
        StockEvent sellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(200))
                .build();

        // when
        marketAggregate.apply(buyEvent);
        marketAggregate.apply(sellEvent);
        marketAggregate.tick();

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertFalse(priceSummaryOptional.isPresent());
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetPriceSummaryReturnValidOpenCloseValues_forBuySellEvents() {
        // given
        StockEvent buyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100))
                .build();
        StockEvent sellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(200))
                .build();

        // when
        marketAggregate.apply(buyEvent);
        marketAggregate.apply(sellEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        assertEquals(100d, priceSummary.getOpen(), 0.00001);
        assertEquals(200d, priceSummary.getClose(), 0.00001);
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetPriceSummaryReturnValidTransactionCountValues_forBuySellEvents() {
        // given
        StockEvent buyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100))
                .build();
        StockEvent sellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(200))
                .build();

        // when
        marketAggregate.apply(buyEvent);
        marketAggregate.apply(sellEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        assertEquals(1, priceSummary.getBuyTransactionCount());
        assertEquals(1, priceSummary.getSellTransactionCount());
    }

    @Test
    public void applyingSeriesOfSingleTypeStockEvents_makesGetPriceSummaryReturnNullOppositeTypePriceSummary_forBuyEvents() {
        // given
        StockEvent buyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(1000))
                .amount(10d)
                .build();

        // when
        marketAggregate.apply(buyEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        assertNull(priceSummary.getSell());
    }

    @Test
    public void applyingSeriesOfSingleTypeStockEvents_makesGetPriceSummaryReturnNullOppositeTypePriceSummary_forSellEvents() {
        // given
        StockEvent sellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(1000))
                .amount(10d)
                .build();

        // when
        marketAggregate.apply(sellEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        assertNull(priceSummary.getBuy());
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetPriceSummaryReturnValidBuyPriceSummary_forBuyEvents() {
        // given
        StockEvent firstBuyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(1000))
                .amount(10d)
                .build();
        StockEvent secondBuyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(100))
                .amount(100d)
                .build();
        StockEvent thirdBuyEvent = StockEvent.builder()
                .type("BUY")
                .price(new DoubleWrapper(10))
                .amount(1000d)
                .build();

        // when
        marketAggregate.apply(firstBuyEvent);
        marketAggregate.apply(secondBuyEvent);
        marketAggregate.apply(thirdBuyEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        PriceSummary.ByTypePriceSummary buyPriceSummary = priceSummary.getBuy();
        assertEquals(10d, buyPriceSummary.getMin());
        assertEquals(1000d, buyPriceSummary.getMax());
        assertEquals(1110d, buyPriceSummary.getVolume());
        assertEquals(370d, buyPriceSummary.getAvg());
        assertEquals(10000d, buyPriceSummary.getWeightedAvg());
    }

    @Test
    public void applyingSeriesOfStockEvents_makesGetPriceSummaryReturnValidSellPriceSummary_forSellEvents() {
        // given
        StockEvent firstSellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(1000))
                .amount(10d)
                .build();
        StockEvent secondSellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(100))
                .amount(100d)
                .build();
        StockEvent thirdSellEvent = StockEvent.builder()
                .type("SELL")
                .price(new DoubleWrapper(10))
                .amount(1000d)
                .build();

        // when
        marketAggregate.apply(firstSellEvent);
        marketAggregate.apply(secondSellEvent);
        marketAggregate.apply(thirdSellEvent);

        // then
        Optional<PriceSummary> priceSummaryOptional = marketAggregate.getPriceSummary();
        assertTrue(priceSummaryOptional.isPresent());
        PriceSummary priceSummary = priceSummaryOptional.get();
        PriceSummary.ByTypePriceSummary sellPriceSummary = priceSummary.getSell();
        assertEquals(10d, sellPriceSummary.getMin());
        assertEquals(1000d, sellPriceSummary.getMax());
        assertEquals(1110d, sellPriceSummary.getVolume());
        assertEquals(370d, sellPriceSummary.getAvg());
        assertEquals(10000d, sellPriceSummary.getWeightedAvg());
    }

    @Test
    public void tickingAggregate_makesGetChangesCountReturnEmptyMap_forAsk() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .type("ASK")
                .price(new DoubleWrapper(100))
                .build();

        // when
        marketAggregate.apply(stockEvent);
        marketAggregate.tick();

        // then
        Map<DoubleWrapper, Integer> changes = marketAggregate.getAsk().getChangesCount(100d);
        assertTrue(changes.isEmpty());
    }

    @Test
    public void tickingAggregate_makesGetChangesCountReturnEmptyMap_forBid() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .type("BID")
                .price(new DoubleWrapper(100))
                .build();

        // when
        marketAggregate.apply(stockEvent);
        marketAggregate.tick();

        // then
        Map<DoubleWrapper, Integer> changes = marketAggregate.getBid().getChangesCount(100d);
        assertTrue(changes.isEmpty());
    }

    @Test
    public void tickingAggregate_makesGetTimeReturnTimeOfNextTimeWindow_forSingleTick() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .timestamp(100L)
                .build();

        // when
        marketAggregate.apply(firstStockEvent);
        marketAggregate.tick();

        // then
        long currentTime = marketAggregate.getCurrentTime();
        assertEquals(currentTime, 1100L);
    }

}
