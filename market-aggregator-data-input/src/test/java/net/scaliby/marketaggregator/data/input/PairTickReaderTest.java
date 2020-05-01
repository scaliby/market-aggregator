package net.scaliby.marketaggregator.data.input;

import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PairTickReaderTest {

    private final KeyResolver<String> keyResolver = new PairStringKeyResolver();

    @Test
    public void reading_returnsEmptyResult_forEmptyReader() {
        // given
        ListStockEventReader reader = ListStockEventReader.builder()
                .build();

        PairTickReader<String> pairTickReader = new PairTickReader<>(reader, keyResolver);

        // when
        List<PairTick<String>> result = pairTickReader.read();

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void reading_returnsSinglePairTickWithTwoEntries_forReaderWithTwoEventsForSinglePair() {
        // given
        StockEvent firstEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .sequence(1L)
                .build();
        StockEvent secondEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .sequence(1L)
                .build();
        ListStockEventReader reader = ListStockEventReader.builder()
                .withStockEvent(firstEvent)
                .withStockEvent(secondEvent)
                .build();

        PairTickReader<String> pairTickReader = new PairTickReader<>(reader, keyResolver);

        // when
        List<PairTick<String>> result = pairTickReader.read();

        // then
        List<PairTick<String>> expected = Collections.singletonList(
                PairTick.of("USD_BTC", Arrays.asList(firstEvent, secondEvent))
        );
        assertEquals(result, expected);
    }

    @Test
    public void reading_returnsSinglePairTickWithSingleEvent_forReaderWithOnlyASingleEventInEachSequence() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .sequence(1L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .sequence(2L)
                .build();
        ListStockEventReader reader = ListStockEventReader.builder()
                .withStockEvent(firstStockEvent)
                .withStockEvent(secondStockEvent)
                .build();

        PairTickReader<String> pairTickReader = new PairTickReader<>(reader, keyResolver);

        // when
        List<PairTick<String>> result = pairTickReader.read();

        // then
        List<PairTick<String>> expected = Collections.singletonList(
                PairTick.of("USD_BTC", Collections.singletonList(firstStockEvent))
        );
        assertEquals(result, expected);
    }

    @Test
    public void reading_returnsTwoPairTicksWithSingleEvent_forReaderWithTwoPairsEventsInSingleSequence() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .sequence(1L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .base("USD")
                .quote("ETH")
                .sequence(1L)
                .build();
        ListStockEventReader reader = ListStockEventReader.builder()
                .withStockEvent(firstStockEvent)
                .withStockEvent(secondStockEvent)
                .build();

        PairTickReader<String> pairTickReader = new PairTickReader<>(reader, keyResolver);

        // when
        List<PairTick<String>> result = pairTickReader.read();

        // then
        List<PairTick<String>> expected = Arrays.asList(
                PairTick.of("USD_BTC", Collections.singletonList(firstStockEvent)),
                PairTick.of("USD_ETH", Collections.singletonList(secondStockEvent))
        );
        assertEquals(result, expected);
    }

}