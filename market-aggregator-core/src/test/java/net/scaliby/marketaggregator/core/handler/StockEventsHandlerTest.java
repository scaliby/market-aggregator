package net.scaliby.marketaggregator.core.handler;

import net.scaliby.marketaggregator.core.market.MarketAggregate;
import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StockEventsHandlerTest {

    @Mock
    private MarketAggregate<String> marketAggregate;
    @Mock
    private MarketHandler<String, Integer> marketHandler;
    @InjectMocks
    private StockEventsHandler<String, Integer> stockEventsHandler;

    @Test
    public void handling_callsAggregateTickUntilMarketCanNotApplyEvents() {
        // given
        StockEvent stockEvent = StockEvent.builder().build();
        given(marketAggregate.canApply(anyList()))
                .willReturn(false, false, true);

        // when
        stockEventsHandler.handle(Collections.singletonList(stockEvent));

        // then
        verify(marketAggregate, times(2)).tick();
    }

    @Test
    public void handling_aggregatesMarketHandlerResultsUntilMarketCanNotApplyEvents() {
        // given
        StockEvent stockEvent = StockEvent.builder().build();
        given(marketAggregate.canApply(anyList()))
                .willReturn(false, false, true);
        given(marketHandler.handle(marketAggregate))
                .willReturn(1, 2, 3);

        // when
        List<Integer> result = stockEventsHandler.handle(Collections.singletonList(stockEvent));

        // then
        List<Integer> expectedResult = Arrays.asList(1, 2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void handling_appliesEventsOnce() {
        // given
        StockEvent stockEvent = StockEvent.builder().build();
        given(marketAggregate.canApply(anyList()))
                .willReturn(true);

        // when
        stockEventsHandler.handle(Collections.singletonList(stockEvent));

        // then
        verify(marketAggregate, times(1))
                .apply(Collections.singletonList(stockEvent));
    }

}