package net.scaliby.marketaggregator.core.market;

import net.scaliby.marketaggregator.core.common.DoubleWrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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
    public void gettingChangesCount_returnsValidMapWithCountedChanges_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        Map<DoubleWrapper, Integer> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 2);
        expectedResult.put(new DoubleWrapper(20), 1);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesCount_returnsValidMapWithCountedChanges_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        Map<DoubleWrapper, Integer> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(30), 2);
        expectedResult.put(new DoubleWrapper(20), 1);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesCountAfterHandlingNullAmount_returnsValidMapWithCountedChanges_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        Map<DoubleWrapper, Integer> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 2);
        expectedResult.put(new DoubleWrapper(20), 1);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesCountAfterHandlingNullAmount_returnsValidMapWithCountedChanges_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), null);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        Map<DoubleWrapper, Integer> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(30), 2);
        expectedResult.put(new DoubleWrapper(20), 1);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesCountAfterTicking_returnsEmptyMap_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.tick();
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void gettingChangesCountAfterTicking_returnsEmptyMap_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        orderBook.tick();
        Map<DoubleWrapper, Integer> result = orderBook.getChangesCount(10);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void gettingChangesAmount_returnsValidMapWithAccumulatedAmounts_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 30d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesAmount_returnsValidMapWithAccumulatedAmounts_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(20), 20d);
        expectedResult.put(new DoubleWrapper(30), 50d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesAmountAfterHandlingNull_returnsValidMapWithAccumulatedAmounts_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), null);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 10d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesAmountAfterHandlingNull_returnsValidMapWithAccumulatedAmounts_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), null);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(20), 20d);
        expectedResult.put(new DoubleWrapper(30), 30d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingChangesAmountAfterTicking_returnsEmptyMap_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void gettingChangesAmountAfterTicking_returnsEmptyMap_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getChangesAmount(10);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void gettingOffers_returnsValidOffersMap_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 20d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffersAfterHandlingNull_returnsValidOffersMap_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), null);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(30), 30d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffersAfterHandlingNull_returnsValidOffersMap_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), null);
        orderBook.handle(new DoubleWrapper(30), 20d);
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffers_returnsValidOffersMap_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(30), 30d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffersAfterTicking_returnsValidOffersMap_forNotInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(false);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(10), 20d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(10), 20d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
    }

    @Test
    public void gettingOffersAfterTicking_returnsValidOffersMap_forInverseOrderBook() {
        // given
        BasicMutableOrderBook orderBook = new BasicMutableOrderBook(true);

        // when
        orderBook.handle(new DoubleWrapper(10), 10d);
        orderBook.handle(new DoubleWrapper(20), 20d);
        orderBook.handle(new DoubleWrapper(30), 20d);
        orderBook.handle(new DoubleWrapper(30), 30d);
        orderBook.tick();
        Map<DoubleWrapper, Double> result = orderBook.getOffers(10);

        // then
        Map<DoubleWrapper, Double> expectedResult = new HashMap<>();
        expectedResult.put(new DoubleWrapper(30), 30d);
        expectedResult.put(new DoubleWrapper(20), 20d);
        assertEquals(expectedResult, result);
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
