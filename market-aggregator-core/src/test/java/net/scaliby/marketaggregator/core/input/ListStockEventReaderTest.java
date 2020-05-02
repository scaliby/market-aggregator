package net.scaliby.marketaggregator.core.input;

import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ListStockEventReaderTest {

    @Test
    public void reading_returnsNull_forEmptyEventsList() {
        // given
        ListStockEventReader reader = new ListStockEventReader(Collections.emptyList());

        // when
        StockEvent result = reader.read();

        // then
        assertNull(result);
    }

    @Test
    public void reading_returnsFirstElementFromList_forNonEmptyEventsList() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .sequence(1L)
                .build();
        ListStockEventReader reader = new ListStockEventReader(Collections.singletonList(stockEvent));

        // when
        StockEvent result = reader.read();

        // then
        assertEquals(stockEvent, result);
    }

    @Test
    public void readingTwice_returnsSecondElementFromList_forNonEmptyEventsList() {
        // given
        StockEvent firstStockEvent = StockEvent.builder()
                .sequence(1L)
                .build();
        StockEvent secondStockEvent = StockEvent.builder()
                .sequence(2L)
                .build();
        ListStockEventReader reader = new ListStockEventReader(Arrays.asList(firstStockEvent, secondStockEvent));

        // when
        reader.read();
        StockEvent result = reader.read();

        // then
        assertEquals(secondStockEvent, result);
    }

}