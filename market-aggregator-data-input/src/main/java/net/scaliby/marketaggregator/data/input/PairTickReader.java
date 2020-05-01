package net.scaliby.marketaggregator.data.input;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.scaliby.marketaggregator.core.market.StockEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PairTickReader<K> {

    private final StockEventReader stockEventReader;
    private final KeyResolver<K> keyResolver;

    private StockEvent nextEvent = null;

    @SneakyThrows
    public List<PairTick<K>> read() {
        if (nextEvent == null) {
            nextEvent = stockEventReader.read();
        }
        Long currentSequence = getCurrentSequence();
        Map<K, List<StockEvent>> result = new HashMap<>();
        while (nextEvent != null && nextEvent.getSequence().equals(currentSequence)) {
            K key = keyResolver.getKey(nextEvent);
            result.computeIfAbsent(key, (a) -> new ArrayList<>()).add(nextEvent);
            nextEvent = stockEventReader.read();
        }
        return result.entrySet().stream()
                .map(row -> PairTick.of(row.getKey(), row.getValue()))
                .collect(Collectors.toList());
    }

    private Long getCurrentSequence() {
        if (nextEvent == null) {
            return null;
        }
        return nextEvent.getSequence();
    }
}
