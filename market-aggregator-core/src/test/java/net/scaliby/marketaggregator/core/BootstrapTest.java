package net.scaliby.marketaggregator.core;

import net.scaliby.marketaggregator.core.common.Timer;
import net.scaliby.marketaggregator.core.handler.ListCollectingDataHandler;
import net.scaliby.marketaggregator.core.handler.StockEventsHandler;
import net.scaliby.marketaggregator.core.input.KeyResolver;
import net.scaliby.marketaggregator.core.input.ListStockEventReader;
import net.scaliby.marketaggregator.core.input.PairStringKeyResolver;
import net.scaliby.marketaggregator.core.input.StockEventReader;
import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BootstrapTest {

    private final KeyResolver<String> keyResolver = new PairStringKeyResolver();
    private final ListCollectingDataHandler<Integer> dataHandler = new ListCollectingDataHandler<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Timer timer = Timer.noOp();
    private final StockEventReader stockEventReader = ListStockEventReader.builder()
            .withStockEvent(StockEvent.builder().sequence(1L).build())
            .withStockEvent(StockEvent.builder().sequence(2L).build())
            .build();
    @Mock
    private StockEventsHandler<Integer> stockEventsHandler;

    @Test
    public void running_passAggregatedDataToHandlerUntilEnd() throws InterruptedException {
        // given
        Bootstrap<String, Integer> bootstrap = Bootstrap.<String, Integer>builder()
                .keyResolver(keyResolver)
                .executor(executor)
                .stockEventReader(stockEventReader)
                .dataHandlerFactory(() -> dataHandler)
                .stockEventsHandlerFactory(() -> stockEventsHandler)
                .timer(timer)
                .build();

        given(stockEventsHandler.handle(anyList()))
                .willReturn(Collections.singletonList(1));

        // when
        bootstrap.run();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // then
        List<Integer> expectedResult = Arrays.asList(1, 1);
        assertEquals(expectedResult, dataHandler.getCollectedData());
    }

}