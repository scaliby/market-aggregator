package net.scaliby.marketaggregator.averageprice.handler;

import org.junit.Test;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AveragePricePrinterDataHandlerTest {

    private PrintWriter printWriter = mock(PrintWriter.class);
    private AveragePricePrinterDataHandler handler = new AveragePricePrinterDataHandler(printWriter);

    @Test
    public void handlingData_printsDataUsingPrintWriter_forNonEmptyOptionalsList() {
        // given
        List<Optional<Double>> data = Collections.singletonList(Optional.of(10d));

        // when
        handler.handle(data);

        // then
        verify(printWriter, times(1))
                .println(any(Double.class));
    }

    @Test
    public void handlingData_printsNothing_forEmptyOptionalsList() {
        // given
        List<Optional<Double>> data = Collections.singletonList(Optional.empty());

        // when
        handler.handle(data);

        // then
        verify(printWriter, never()).println(anyDouble());
    }

}