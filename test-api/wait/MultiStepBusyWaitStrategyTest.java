package eu.menzani.ringbuffer.wait;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static perftest.wait.MultiStepBusyWaitStrategyTest.*;

abstract class MultiStepBusyWaitStrategyTest {
    private final perftest.wait.MultiStepBusyWaitStrategyTest test;
    private final List<TestBusyWaitStrategy.Event> events = new ArrayList<>();
    private Iterator<TestBusyWaitStrategy.Event> eventIterator;

    MultiStepBusyWaitStrategyTest(perftest.wait.MultiStepBusyWaitStrategyTest test) {
        this.test = test;
    }

    @Test
    void testResetAndTick() {
        FIRST = new TestBusyWaitStrategy("first");
        SECOND = new TestBusyWaitStrategy("second");
        THIRD = new TestBusyWaitStrategy("third");
        FOURTH = new TestBusyWaitStrategy("fourth");
        FIFTH = new TestBusyWaitStrategy("fifth");
        SIXTH = new TestBusyWaitStrategy("sixth");

        final int numIterations = 2;
        test.test(numIterations, 1);
        eventIterator = events.iterator();

        assertEquals((STEP_TICKS * 7 + 6) * numIterations, events.size(), events::toString);
        for (int i = 0; i < numIterations; i++) {
            expectReset(FIRST, i, STEP_TICKS);
            expectTick(STEP_TICKS, FIRST);
            expectReset(SECOND, i, STEP_TICKS);
            expectTick(STEP_TICKS, SECOND);
            expectReset(THIRD, i, STEP_TICKS);
            expectTick(STEP_TICKS, THIRD);
            expectReset(FOURTH, i, STEP_TICKS);
            expectTick(STEP_TICKS, FOURTH);
            expectReset(FIFTH, i, STEP_TICKS);
            expectTick(STEP_TICKS, FIFTH);
            expectReset(SIXTH, i, STEP_TICKS + STEP_TICKS);
            expectTick(STEP_TICKS + STEP_TICKS, SIXTH);
        }
    }

    private void expectTick(int times, BusyWaitStrategy strategy) {
        TestBusyWaitStrategy testStrategy = (TestBusyWaitStrategy) strategy;
        for (int i = 1; i <= times; i++) {
            assertEquals(testStrategy.new Event(false, i), eventIterator.next(), events::toString);
        }
    }

    private void expectReset(BusyWaitStrategy strategy, int numIteration, int tickTimes) {
        TestBusyWaitStrategy testStrategy = (TestBusyWaitStrategy) strategy;
        assertEquals(testStrategy.new Event(true, numIteration == 0 ? 0 : tickTimes + 1), eventIterator.next(), events::toString);
    }

    public class TestBusyWaitStrategy implements BusyWaitStrategy {
        private final String name;
        private int counter;

        TestBusyWaitStrategy(String name) {
            this.name = name;
        }

        @Override
        public void reset() {
            events.add(new Event(true, counter));
            counter = 1;
        }

        @Override
        public void tick() {
            events.add(new Event(false, counter));
            counter++;
        }

        private class Event {
            private final boolean resetOrTick;
            private final int counter;

            Event(boolean resetOrTick, int counter) {
                this.resetOrTick = resetOrTick;
                this.counter = counter;
            }

            private TestBusyWaitStrategy getStrategy() {
                return TestBusyWaitStrategy.this;
            }

            @Override
            public boolean equals(Object object) {
                Event that = (Event) object;
                return getStrategy() == that.getStrategy() && resetOrTick == that.resetOrTick && counter == that.counter;
            }

            @Override
            public String toString() {
                return '\n' + (resetOrTick ? "Reset " : "Tick ") + name + " - " + counter;
            }
        }
    }
}
