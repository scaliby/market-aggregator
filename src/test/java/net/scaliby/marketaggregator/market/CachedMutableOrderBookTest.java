package net.scaliby.marketaggregator.market;

import net.scaliby.marketaggregator.common.DoubleWrapper;
import org.junit.Test;

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
