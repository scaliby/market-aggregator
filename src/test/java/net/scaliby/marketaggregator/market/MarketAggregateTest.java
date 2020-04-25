package net.scaliby.marketaggregator.market;

import net.scaliby.marketaggregator.common.DoubleWrapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.*;

public class MarketAggregateTest {

    private MarketAggregate marketAggregate = new MarketAggregate(
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
                .id(1L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .type("ASK")
                .id(2L)
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
