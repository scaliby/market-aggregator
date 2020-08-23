package net.scaliby.marketaggregator.core.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComparatorCommon {

    public static Comparator<DoubleWrapper> getComparator(boolean inverse) {
        if (inverse) {
            return Comparator.reverseOrder();
        }
        return Comparator.naturalOrder();
    }

}
