package net.scaliby.marketaggregator.core.input;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class PairTick<T> {

    private final T key;
    private final List<StockEvent> events;

}
