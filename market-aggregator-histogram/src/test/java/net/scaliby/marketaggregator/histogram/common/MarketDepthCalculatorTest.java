package net.scaliby.marketaggregator.histogram.common;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import net.scaliby.marketaggregator.core.market.BasicMutableOrderBook;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class MarketDepthCalculatorTest {

    @Test
    public void gettingMarketDepthWithEmptyOrderBook_returnsArrayWithZeros() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        double[] expectedResult = new double[]{0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithoutHavingToSkipEntry_returnsValidArray_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(15), 5d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11  14  17  20
        double[] expectedResult = new double[]{0, 0, 10, 10, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithHavingToSkipEntry_returnsValidArray_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(9), 10d);
        orderBook.handle(new DoubleWrapper(10), 5d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11  14  17  20
        double[] expectedResult = new double[]{0, 0, 15, 15, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithoutHavingToSkipEntry_returnsValidArray_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(15), 10d);
        orderBook.handle(new DoubleWrapper(10), 5d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                       price points: 20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 10, 10, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithHavingToSkipEntry_returnsValidArray_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(16), 10d);
        orderBook.handle(new DoubleWrapper(15), 5d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                       price points: 20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 15, 15, 15, 15};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithNullValueEntryHandled_returnsValidArrayWithRemovedPricePoint_forInverseNotOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(15), 20d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(5), new DoubleWrapper(3));

        // then
        //                       price points: 5  8  11 14 17  20
        double[] expectedResult = new double[]{0, 0, 0, 0, 20, 20};
        assertArrayEquals(expectedResult, result, 0.01);
    }

    @Test
    public void gettingMarketDepthWithNullValueEntryHandled_returnsValidArrayWithRemovedPricePoint_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);
        MarketDepthCalculator marketDepthCalculator = new MarketDepthCalculator(orderBook);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(15), 20d);
        double[] result = marketDepthCalculator.getMarketDepth(6, new DoubleWrapper(20), new DoubleWrapper(3));

        // then
        //                      price points:  20 17 14  11   8   5
        double[] expectedResult = new double[]{0, 0, 20, 20, 20, 20};
        assertArrayEquals(expectedResult, result, 0.01);
    }

}