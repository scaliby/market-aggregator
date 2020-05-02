package net.scaliby.marketaggregator.core.input;

import net.scaliby.marketaggregator.core.market.StockEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairStringKeyResolverTest {

    private final PairStringKeyResolver resolver = new PairStringKeyResolver();

    @Test
    public void resolvingKey_returnsConcatenatedBaseAndQuote() {
        // given
        StockEvent stockEvent = StockEvent.builder()
                .base("USD")
                .quote("BTC")
                .build();

        // when
        String result = resolver.getKey(stockEvent);

        // then
        assertEquals("USD_BTC", result);
    }

}