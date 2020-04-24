package net.scaliby.marketaggregator.market;

import lombok.*;
import net.scaliby.marketaggregator.common.DoubleWrapper;

@Builder
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor(staticName = "of")
public class StockEvent {

    @Builder.Default
    private final Long id = 123L;
    @Builder.Default
    private final Long sequence = 123L;
    @Builder.Default
    private final String type = "ASK";
    @Builder.Default
    private final String tradeId = null;
    @Builder.Default
    private final DoubleWrapper price = new DoubleWrapper(123);
    @Builder.Default
    private final Double amount = 10d;
    @Builder.Default
    private final Long timestamp = 321L;
    @Builder.Default
    private final String base = "BTC";
    @Builder.Default
    private final String quote = "USD";

}
