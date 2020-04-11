package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static test.wait.MultiStepBusyWaitStrategyTest.*;

abstract class MultiStepBusyWaitStrategyTest {
    private final test.wait.MultiStepBusyWaitStrategyTest performanceTest;
    private Iterator<TestBusyWaitStrategy.Event> eventIterator;
    private final List<TestBusyWaitStrategy.Event> events = new ArrayList<>();

    MultiStepBusyWaitStrategyTest(test.wait.MultiStepBusyWaitStrategyTest performanceTest) {
        this.performanceTest = performanceTest;
    }

    @Test
    void testResetAndTick() {
        FIRST = new TestBusyWaitStrategy("first");
        SECOND = new TestBusyWaitStrategy("second");
        THIRD = new TestBusyWaitStrategy("third");
        FOURTH = new TestBusyWaitStrategy("fourth");
        FIFTH = new TestBusyWaitStrategy("fifth");
        SIXTH = new TestBusyWaitStrategy("sixth");
        BusyWaitStrategy[] steps = {FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH};

        final int numIterations = 2;
        int numSteps = performanceTest.getNumSteps();
        Assert.notGreater(numSteps, steps.length);
        performanceTest.run(numIterations, 1);
        eventIterator = events.iterator();

        int numStepsMinusOne = numSteps - 1;
        for (int i = 0; i < numIterations; i++) {
            for (int j = 0; j < numStepsMinusOne; j++) {
                TestBusyWaitStrategy step = (TestBusyWaitStrategy) steps[j];
                expectReset(step, i, STEP_TICKS);
                expectTick(STEP_TICKS, step);
            }
            TestBusyWaitStrategy step = (TestBusyWaitStrategy) steps[numStepsMinusOne];
            int lastStepTickTimes = NUM_TICKS - STEP_TICKS * numStepsMinusOne;
            expectReset(step, i, lastStepTickTimes);
            expectTick(lastStepTickTimes, step);
        }
        assertEquals((NUM_TICKS + numSteps) * numIterations, events.size(), events::toString);
        assertThrows(NoSuchElementException.class, () -> eventIterator.next(), events::toString);
    }

    private void expectTick(int times, TestBusyWaitStrategy step) {
        for (int i = 1; i <= times; i++) {
            assertEquals(step.new Event(false, i), eventIterator.next(), events::toString);
        }
    }

    private void expectReset(TestBusyWaitStrategy step, int numIteration, int tickTimes) {
        assertEquals(step.new Event(true, numIteration == 0 ? 0 : tickTimes + 1), eventIterator.next(), events::toString);
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
