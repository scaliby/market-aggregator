package net.scaliby.marketaggregator.core.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleWrapperTest {

    @Test
    public void serializingToString_returnsValidRepresentation_forStringConstructor() {
        // given
        String value = "123.02";
        DoubleWrapper doubleWrapper = new DoubleWrapper(value);

        // when
        String result = doubleWrapper.toString();

        // then
        assertEquals(value, result);
    }

    @Test
    public void equalityCheck_returnsTrue_forDoubleWrappersWithTheSameStringValue() {
        // given
        String value = "123.02";
        DoubleWrapper doubleWrapperA = new DoubleWrapper(value);
        DoubleWrapper doubleWrapperB = new DoubleWrapper(value);

        // when
        boolean equals = doubleWrapperA.equals(doubleWrapperB);

        // then
        assertTrue(equals);
    }

    @Test
    public void equalityCheck_returnsFalse_forDoubleWrappersWithDifferentStringValues() {
        // given
        DoubleWrapper doubleWrapperA = new DoubleWrapper("123.02");
        DoubleWrapper doubleWrapperB = new DoubleWrapper("123.01");

        // when
        boolean equals = doubleWrapperA.equals(doubleWrapperB);

        // then
        assertFalse(equals);
    }

    @Test
    public void equalityCheck_returnsTrue_forDoubleWrappersWithTheSameDoubleValue() {
        // given
        double value = 123.02d;
        DoubleWrapper doubleWrapperA = new DoubleWrapper(value);
        DoubleWrapper doubleWrapperB = new DoubleWrapper(value);

        // when
        boolean equals = doubleWrapperA.equals(doubleWrapperB);

        // then
        assertTrue(equals);
    }

    @Test
    public void equalityCheck_returnsFalse_forDoubleWrapperWithDifferentDoubleValues() {
        // given
        DoubleWrapper doubleWrapperA = new DoubleWrapper(123.02d);
        DoubleWrapper doubleWrapperB = new DoubleWrapper(123.01d);

        // when
        boolean equals = doubleWrapperA.equals(doubleWrapperB);

        // then
        assertFalse(equals);
    }

    @Test
    public void comparing_returnsZero_forDoubleWrappersWithTheSameWrapper() {
        // given
        DoubleWrapper doubleWrapperA = new DoubleWrapper("123.01");
        DoubleWrapper doubleWrapperB = new DoubleWrapper("123.01");

        // when
        int result = doubleWrapperA.compareTo(doubleWrapperB);

        // then
        assertEquals(0, result);
    }

    @Test
    public void comparing_returnsPositiveOne_forComparisonWithSmallerWrapper() {
        // given
        DoubleWrapper doubleWrapperA = new DoubleWrapper("123.01");
        DoubleWrapper doubleWrapperB = new DoubleWrapper("120.01");

        // when
        int result = doubleWrapperA.compareTo(doubleWrapperB);

        // then
        assertEquals(1, result);
    }

    @Test
    public void comparing_returnsNegativeOne_forComparisonWithLargerWrapper() {
        // given
        DoubleWrapper doubleWrapperA = new DoubleWrapper("123.01");
        DoubleWrapper doubleWrapperB = new DoubleWrapper("125.01");

        // when
        int result = doubleWrapperA.compareTo(doubleWrapperB);

        // then
        assertEquals(-1, result);
    }

}