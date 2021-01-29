package org.ringbuffer.wait;

import eu.menzani.lang.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static test.wait.MultiStepBusyWaitStrategyTest.NUM_TICKS;
import static test.wait.MultiStepBusyWaitStrategyTest.STEP_TICKS;

abstract class MultiStepBusyWaitStrategyTest {
    private final test.wait.MultiStepBusyWaitStrategyTest performanceTest;
    private Iterator<TestBusyWaitStrategy.Event> eventIterator;
    private final List<TestBusyWaitStrategy.Event> events = new ArrayList<>();

    MultiStepBusyWaitStrategyTest(test.wait.MultiStepBusyWaitStrategyTest performanceTest) {
        this.performanceTest = performanceTest;
    }

    public void testResetAndTick() {
        performanceTest.first = new TestBusyWaitStrategy("first");
        performanceTest.second = new TestBusyWaitStrategy("second");
        performanceTest.third = new TestBusyWaitStrategy("third");
        performanceTest.fourth = new TestBusyWaitStrategy("fourth");
        performanceTest.fifth = new TestBusyWaitStrategy("fifth");
        performanceTest.sixth = new TestBusyWaitStrategy("sixth");
        BusyWaitStrategy[] steps = {performanceTest.first, performanceTest.second, performanceTest.third,
                performanceTest.fourth, performanceTest.fifth, performanceTest.sixth};

        performanceTest.runBenchmark();
        eventIterator = events.iterator();

        int numIterations = performanceTest.getNumIterations();
        int numSteps = performanceTest.getNumSteps();
        Assert.notGreater(numSteps, steps.length);
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
        assert events.size() == (NUM_TICKS + numSteps) * numIterations : events;
        Assert.fails(() -> eventIterator.next(), NoSuchElementException.class, events::toString);
    }

    private void expectTick(int times, TestBusyWaitStrategy step) {
        for (int i = 1; i <= times; i++) {
            assert eventIterator.next().equals(step.new Event(false, i)) : events;
        }
    }

    private void expectReset(TestBusyWaitStrategy step, int numIteration, int tickTimes) {
        assert eventIterator.next().equals(step.new Event(true, numIteration == 0 ? 0 : tickTimes + 1)) : events;
    }

    private class TestBusyWaitStrategy implements BusyWaitStrategy {
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
