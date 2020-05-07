package net.scaliby.marketaggregator.core.handler;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListCollectingDataHandlerTest {

    private final ListCollectingDataHandler<String> handler = new ListCollectingDataHandler<>();

    @Test
    public void handling_makesGettingCollectedDataReturnHandledValue() {
        // given
        List<String> data = Collections.singletonList("Data");

        // when
        handler.handle(data);

        // then
        assertEquals(data, handler.getCollectedData());
    }

}