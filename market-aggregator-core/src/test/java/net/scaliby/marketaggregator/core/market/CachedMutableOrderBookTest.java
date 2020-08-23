package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CachedMutableOrderBookTest {

    private final MutableOrderBook delegate = mock(MutableOrderBook.class);
    private final CachedMutableOrderBook orderBook = new CachedMutableOrderBook(delegate);

    @Test
    public void handling_executesDelegate_forAnyPriceAndAmount() {
        // given
        DoubleWrapper price = new DoubleWrapper(123);
        Double amount = 123d;

        // when
        orderBook.handle(price, amount);

        // then
        verify(delegate, atLeastOnce()).handle(price, amount);
    }

    @Test
    public void gettingTotalAmountInBaseCurrency_returnsValueReturnedByDelegate() {
        // given
        double expectedTotalAmountInBaseCurrency = 1233d;

        given(delegate.getTotalAmountInBaseCurrency())
                .willReturn(expectedTotalAmountInBaseCurrency);

        // when
        double result = orderBook.getTotalAmountInBaseCurrency();

        // then
        assertEquals(expectedTotalAmountInBaseCurrency, result, 0.0000001);
    }

    @Test
    public void gettingTotalAmountInBaseCurrencyAfterHandling_returnsNewlyCalculatedValue() {
        // given
        double expectedTotalAmountInBaseCurrency = 1233d;

        given(delegate.getTotalAmountInBaseCurrency())
                .willReturn(123d, expectedTotalAmountInBaseCurrency);

        // when
        orderBook.getTotalAmountInBaseCurrency();
        orderBook.handle(new DoubleWrapper(123), 123d);
        double result = orderBook.getTotalAmountInBaseCurrency();

        // then
        assertEquals(expectedTotalAmountInBaseCurrency, result, 0.0000001);
    }

    @Test
    public void gettingTotalAmountInBaseCurrency_returnsValueFromFirstDelegateInvocation() {
        // given
        double expectedTotalAmountInBaseCurrency = 1233d;

        given(delegate.getTotalAmountInBaseCurrency())
                .willReturn(expectedTotalAmountInBaseCurrency, 123d);

        // when
        orderBook.getTotalAmountInBaseCurrency();
        double result = orderBook.getTotalAmountInBaseCurrency();

        // then
        assertEquals(expectedTotalAmountInBaseCurrency, result, 0.0000001);
    }

    @Test
    public void gettingTotalAmountInBaseCurrency_invokesDelegateOnlyOnce() {
        // when
        orderBook.getTotalAmountInBaseCurrency();
        orderBook.getTotalAmountInBaseCurrency();

        // then
        verify(delegate, atMostOnce()).getTotalAmountInBaseCurrency();
    }

    @Test
    public void gettingChangesCount_returnsMapReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Integer> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 10);

        given(delegate.getChangesCount(limitPrice))
                .willReturn(expectedResult);

        // when
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(limitPrice);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesCountAfterTicking_returnsNewValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Integer> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20);

        Map<DoubleWrapper, Integer> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30);

        given(delegate.getChangesCount(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getChangesCount(limitPrice);
        orderBook.tick();
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(limitPrice);

        // then
        assertEquals(secondResult, result);
    }

    @Test
    public void gettingChangesCountAfterHandling_returnsNewValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Integer> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20);

        Map<DoubleWrapper, Integer> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30);

        given(delegate.getChangesCount(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getChangesCount(limitPrice);
        orderBook.handle(new DoubleWrapper(20), 10d);
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(limitPrice);

        // then
        assertEquals(secondResult, result);
    }

    @Test
    public void gettingChangesCountTwiceWithTheSameParams_invokesDelegateOnlyOnce() {
        // given
        double limitPrice = 10d;

        // when
        orderBook.getChangesCount(limitPrice);
        orderBook.getChangesCount(limitPrice);

        // then
        verify(delegate, atMostOnce()).getChangesCount(limitPrice);
    }

    @Test
    public void gettingChangesAmount_returnsMapReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 10d);

        given(delegate.getChangesAmount(limitPrice))
                .willReturn(expectedResult);

        // when
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(limitPrice);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesAmountAfterTicking_returnsNewValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20d);

        Map<DoubleWrapper, Double> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30d);

        given(delegate.getChangesAmount(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getChangesAmount(limitPrice);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(limitPrice);

        // then
        assertEquals(secondResult, result);
    }

    @Test
    public void gettingChangesAmountAfterHandling_returnsNewValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20d);

        Map<DoubleWrapper, Double> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30d);

        given(delegate.getChangesAmount(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getChangesAmount(limitPrice);
        orderBook.handle(new DoubleWrapper(20), 10d);
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(limitPrice);

        // then
        assertEquals(secondResult, result);
    }

    @Test
    public void gettingChangesAmountTwiceWithTheSameParams_invokesDelegateOnlyOnce() {
        // given
        double limitPrice = 10d;

        // when
        orderBook.getChangesAmount(limitPrice);
        orderBook.getChangesAmount(limitPrice);

        // then
        verify(delegate, atMostOnce()).getChangesAmount(limitPrice);
    }

    @Test
    public void gettingOffers_returnsMapReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 10d);

        given(delegate.getOffers(limitPrice))
                .willReturn(expectedResult);

        // when
        Map<DoubleWrapper, Double> result = orderBook.getOffers(limitPrice);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffersAfterTicking_returnsOldValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20d);

        Map<DoubleWrapper, Double> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30d);

        given(delegate.getOffers(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getOffers(limitPrice);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getOffers(limitPrice);

        // then
        assertEquals(firstResult, result);
    }

    @Test
    public void gettingOffersAfterHandling_returnsNewValueReturnedByDelegate() {
        // given
        double limitPrice = 10d;

        Map<DoubleWrapper, Double> firstResult = new HashMap<>();
        firstResult.put(new DoubleWrapper(10), 20d);

        Map<DoubleWrapper, Double> secondResult = new HashMap<>();
        secondResult.put(new DoubleWrapper(20), 30d);

        given(delegate.getOffers(limitPrice))
                .willReturn(firstResult, secondResult);

        // when
        orderBook.getOffers(limitPrice);
        orderBook.handle(new DoubleWrapper(20), 10d);
        Map<DoubleWrapper, Double> result = orderBook.getOffers(limitPrice);

        // then
        assertEquals(secondResult, result);
    }

    @Test
    public void gettingOffersTwiceWithTheSameParams_invokesDelegateOnlyOnce() {
        // given
        double limitPrice = 10d;

        // when
        orderBook.getOffers(limitPrice);
        orderBook.getOffers(limitPrice);

        // then
        verify(delegate, atMostOnce()).getOffers(limitPrice);
    }

    @Test
    public void ticking_ticksDelegate() {
        // when
        orderBook.tick();

        // then
        verify(delegate, times(1)).tick();
    }

    @Test
    public void gettingMarketDepth_returnsDataReturnedByDelegate() {
        // given
        int samples = 1;
        DoubleWrapper startingPrice = new DoubleWrapper(2);
        DoubleWrapper step = new DoubleWrapper(3);
        double[] expectedResult = new double[]{1, 2, 3};

        given(delegate.getMarketDepth(samples, startingPrice, step))
                .willReturn(expectedResult);

        // when
        double[] result = orderBook.getMarketDepth(samples, startingPrice, step);

        // then
        assertArrayEquals(expectedResult, result, 0.0000001);
    }

    @Test
    public void gettingMarketDepthWithSameParameters_returnsValueFromFirstDelegateInvocation() {
        // given
        int samples = 1;
        DoubleWrapper startingPrice = new DoubleWrapper(2);
        DoubleWrapper step = new DoubleWrapper(3);
        double[] expectedResult = new double[]{1, 2, 3};

        given(delegate.getMarketDepth(samples, startingPrice, step))
                .willReturn(expectedResult, new double[]{2});

        // when
        double[] result = orderBook.getMarketDepth(samples, startingPrice, step);

        // then
        assertArrayEquals(expectedResult, result, 0.0000001);
    }

    @Test
    public void gettingMarketDepthWithSameParametersAfterHandling_returnsValueFromSecondDelegateInvocation() {
        // given
        int samples = 1;
        DoubleWrapper startingPrice = new DoubleWrapper(2);
        DoubleWrapper step = new DoubleWrapper(3);
        double[] expectedResult = new double[]{1, 2, 3};

        given(delegate.getMarketDepth(samples, startingPrice, step))
                .willReturn(new double[]{2}, expectedResult);

        // when
        orderBook.getMarketDepth(samples, startingPrice, step);
        orderBook.handle(new DoubleWrapper(23), 123d);
        double[] result = orderBook.getMarketDepth(samples, startingPrice, step);

        // then
        assertArrayEquals(expectedResult, result, 0.0000001);
    }

    @Test
    public void gettingMarketDepthWithDifferentParameters_returnsValueSpecifiedForParameters() {
        // given
        int samples = 1;
        DoubleWrapper startingPrice = new DoubleWrapper(2);
        DoubleWrapper step = new DoubleWrapper(3);
        double[] expectedResult = new double[]{1, 2, 3};

        given(delegate.getMarketDepth(anyInt(), any(DoubleWrapper.class), any(DoubleWrapper.class)))
                .willReturn(new double[]{2});
        given(delegate.getMarketDepth(samples, startingPrice, step))
                .willReturn(expectedResult);

        // when
        orderBook.getMarketDepth(123, new DoubleWrapper(321), new DoubleWrapper(123));
        orderBook.handle(new DoubleWrapper(23), 123d);
        double[] result = orderBook.getMarketDepth(samples, startingPrice, step);

        // then
        assertArrayEquals(expectedResult, result, 0.0000001);
    }
}
