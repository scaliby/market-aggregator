package net.scaliby.marketaggregator.core.common;

import lombok.Getter;

public class DoubleWrapper implements Comparable<DoubleWrapper> {

    public static final DoubleWrapper ZERO = new DoubleWrapper("0");
    public static final DoubleWrapper ONE = new DoubleWrapper("1");

    @Getter
    private final double value;
    private final String stringValue;

    public DoubleWrapper(String stringValue) {
        this.value = Double.parseDouble(stringValue);
        this.stringValue = stringValue;
    }

    public DoubleWrapper(double value) {
        this.value = value;
        this.stringValue = String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return stringValue.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleWrapper that = (DoubleWrapper) o;
        return stringValue.equals(that.stringValue);
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public int compareTo(DoubleWrapper doubleWrapper) {
        return Double.compare(value, doubleWrapper.value);
    }
}
