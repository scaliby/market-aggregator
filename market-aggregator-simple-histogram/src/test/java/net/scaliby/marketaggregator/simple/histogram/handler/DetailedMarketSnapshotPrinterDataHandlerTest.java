package net.scaliby.marketaggregator.simple.histogram.handler;

import net.scaliby.marketaggregator.simple.histogram.common.DetailedMarketSnapshot;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DetailedMarketSnapshotPrinterDataHandlerTest {
    private final PrintWriter printWriter = mock(PrintWriter.class);
    private final DetailedMarketSnapshotPrinterDataHandler<String> handler = new DetailedMarketSnapshotPrinterDataHandler<>(printWriter);

    @Test
    public void handlingData_printsDataUsingPrintWriter_forNonEmptyOptionalsList() {
        // given
        DetailedMarketSnapshot<String> marketSnapshot = DetailedMarketSnapshot.<String>builder()
                .key("BTC_USD")
                .build();

        // when
        handler.handle(Collections.singletonList(marketSnapshot));

        // then
        verify(printWriter, times(1))
                .println(marketSnapshot);
    }

    @Test
    public void handlingData_printsNothing_forEmptyOptionalsList() {
        // given
        List<DetailedMarketSnapshot<String>> data = Collections.emptyList();

        // when
        handler.handle(data);

        // then
        verify(printWriter, never()).println(any(DetailedMarketSnapshot.class));
    }

}