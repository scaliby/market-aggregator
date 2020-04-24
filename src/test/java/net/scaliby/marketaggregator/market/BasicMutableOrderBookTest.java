package net.scaliby.marketaggregator.market;

import net.scaliby.marketaggregator.common.DoubleWrapper;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BasicMutableOrderBookTest {

    @Test
    public void gettingTotalAmountInBaseCurrency_returnsValidValue_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 5d);
        double result = orderBook.getTotalAmountInBaseCurrency();

        // then
        assertEquals(15, result, 0.001);
    }

    @Test
    public void gettingTotalAmountInBaseCurrency_returnsValidValue_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 5d);
        double result = orderBook.getTotalAmountInBaseCurrency();

        // then
        assertEquals(200, result, 0.001);
    }

    @Test
    public void gettingMarketDepthWithoutHavingToSkipEntry_returnsValidArray_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(15), 5d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11  14  17  20
        double[] expectedResult = new double[]{0, 0, 10, 10, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithHavingToSkipEntry_returnsValidArray_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(9), 10d);
        orderBook.handle(new DoubleWrapper(10), 5d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11  14  17  20
        double[] expectedResult = new double[]{0, 0, 15, 15, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithoutHavingToSkipEntry_returnsValidArray_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(15), 10d);
        orderBook.handle(new DoubleWrapper(10), 5d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                       price points: 20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 10, 10, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithHavingToSkipEntry_returnsValidArray_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(16), 10d);
        orderBook.handle(new DoubleWrapper(15), 5d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                       price points: 20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 15, 15, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithNullValueEntryHandled_returnsValidArrayWithRemovedPricePoint_forInverseNotOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(15), 20d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11 14 17  20
        double[] expectedResult = new double[]{0, 0, 0, 0, 20, 20};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithNullValueEntryHandled_returnsValidArrayWithRemovedPricePoint_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(15), 20d);
        double[] result = orderBook.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                      price points:  20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 20, 20, 20, 20};
        assertArrayEquals(expectedResult, result, 0.01);
    }

}
