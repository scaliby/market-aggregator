package net.scaliby.marketaggregator.core.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConcurrentExecutionsCounterTest {

    @Test
    public void startingExecutionBeforeFinishingPrevious_returnsTwoAsMaxCount_forTwoExecutions() {
        // given
        ConcurrentExecutionsCounter counter = new ConcurrentExecutionsCounter();

        // when
        counter.executionStarted();
        counter.executionStarted();
        counter.executionFinished();
        counter.executionFinished();

        // then
        assertEquals(2, counter.getMaxCount());
    }

    @Test
    public void startingExecutionAfterFinishingPrevious_returnsOneAsMaxCount_forTwoExecutions() {
        // given
        ConcurrentExecutionsCounter counter = new ConcurrentExecutionsCounter();

        // when
        counter.executionStarted();
        counter.executionFinished();
        counter.executionStarted();
        counter.executionFinished();

        // then
        assertEquals(1, counter.getMaxCount());
    }

}