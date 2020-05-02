package net.scaliby.marketaggregator.core.infrastructure;

import net.scaliby.marketaggregator.core.handler.DataHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StockEventsChannelRunnableTest {

    @Mock
    private StockEventsHandler<Integer> stockEventsHandler;
    @Mock
    private DataHandler<Integer> dataHandler;

    @Test
    public void running_handlesEventsHandlerResultsWithDataHandler() {
        // given
        List<StockEvent> stockEvents = Collections.singletonList(
                StockEvent.builder()
                        .sequence(1L)
                        .build()
        );
        List<Integer> eventsHandlingResult = Collections.singletonList(1337);

        StockEventsChannelRunnable<String, Integer> runnable = new StockEventsChannelRunnable<>(
                "CHANNEL",
                stockEvents,
                stockEventsHandler,
                dataHandler
        );

        given(stockEventsHandler.handle(stockEvents))
                .willReturn(eventsHandlingResult);

        // when
        runnable.run();

        // then
        verify(dataHandler, times(1))
                .handle(eventsHandlingResult);
    }

}