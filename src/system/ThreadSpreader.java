package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.concurrent.AtomicInt;
import eu.menzani.ringbuffer.java.Ensure;

import java.util.function.IntUnaryOperator;

public class ThreadSpreader {
    private final int firstCPU;
    private final int lastCPU;
    private final int increment;
    private final boolean cycle;
    private final AtomicInt nextCPU;

    ThreadSpreader(Builder builder) {
        firstCPU = builder.firstCPU;
        lastCPU = builder.lastCPU;
        increment = builder.increment;
        cycle = builder.cycle;
        nextCPU = new AtomicInt(firstCPU);
    }

    public int bindCurrentThreadToNextCPU() {
        int cpu = nextCPU();
        Threads.bindCurrentThreadToCPU(cpu);
        return cpu;
    }

    int nextCPU() {
        return nextCPU.getAndUpdate(updateFunction);
    }

    private final IntUnaryOperator updateFunction = new IntUnaryOperator() {
        @Override
        public int applyAsInt(int cpu) {
            if (cpu > lastCPU) {
                throw new ThreadManipulationException("No more CPUs are available to bind to.");
            }
            int next = cpu + increment;
            if (cycle && next > lastCPU) {
                return firstCPU;
            }
            return next;
        }
    };

    public void reset() {
        nextCPU.setVolatile(firstCPU);
    }

    public static class Builder {
        private int firstCPU = -1;
        private int increment = -1;
        private int lastCPU = -1;
        private boolean cycle;

        public Builder fromCPU(int firstCPU) {
            Assume.notNegative(firstCPU);
            this.firstCPU = firstCPU;
            return this;
        }

        public Builder fromFirstCPU() {
            fromCPU(0);
            return this;
        }

        public Builder fromSecondCPU() {
            ensureIncrementWasSet();
            fromCPU(increment);
            return this;
        }

        public Builder increment(int increment) {
            Assume.notLesser(increment, 1);
            this.increment = increment;
            return this;
        }

        public Builder skipHyperthreads() {
            increment(2);
            return this;
        }

        public Builder skipFourWayHyperthreads() {
            increment(4);
            return this;
        }

        public Builder toCPU(int lastCPU) {
            Assume.notNegative(lastCPU);
            this.lastCPU = lastCPU;
            return this;
        }

        public Builder toLastCPU() {
            toCPU(Runtime.getRuntime().availableProcessors() - 1);
            return this;
        }

        public Builder cycle() {
            cycle = true;
            return this;
        }

        public ThreadSpreader build() {
            if (firstCPU == -1) {
                throw new IllegalStateException("The first CPU was not set.");
            }
            ensureIncrementWasSet();
            if (lastCPU == -1) {
                throw new IllegalStateException("The last CPU was not set.");
            }
            Ensure.notGreater(firstCPU, lastCPU);
            return new ThreadSpreader(this);
        }

        private void ensureIncrementWasSet() {
            if (increment == -1) {
                throw new IllegalStateException("The increment was not set.");
            }
        }
    }
}
