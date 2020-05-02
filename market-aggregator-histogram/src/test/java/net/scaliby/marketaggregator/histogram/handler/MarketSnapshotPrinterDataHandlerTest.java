package net.scaliby.marketaggregator.histogram.handler;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.histogram.common.MarketSnapshot;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MarketSnapshotPrinterDataHandlerTest {
    private PrintWriter printWriter = mock(PrintWriter.class);
    private MarketSnapshotPrinterDataHandler handler = new MarketSnapshotPrinterDataHandler(printWriter);

    @Test
    public void handlingData_printsDataUsingPrintWriter_forNonEmptyOptionalsList() {
        // given
        MarketSnapshot marketSnapshot = MarketSnapshot.of(
                "BTC",
                "USD",
                new double[][]{},
                new double[][]{},
                1000L,
                new DoubleWrapper(10),
                new DoubleWrapper(20)
        );

        // when
        handler.handle(Collections.singletonList(marketSnapshot));

        // then
        verify(printWriter, times(1))
                .println(marketSnapshot);
    }

    @Test
    public void handlingData_printsNothing_forEmptyOptionalsList() {
        // given
        List<MarketSnapshot> data = Collections.emptyList();

        // when
        handler.handle(data);

        // then
        verify(printWriter, never()).println(any(MarketSnapshot.class));
    }

}