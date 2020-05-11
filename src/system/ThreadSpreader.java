package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Ensure;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class ThreadSpreader {
    private final int firstCPU;
    private final int lastCPU;
    private final int increment;
    private final boolean cycle;
    private final AtomicInteger nextCPU;

    ThreadSpreader(Builder builder) {
        firstCPU = builder.firstCPU;
        lastCPU = builder.lastCPU;
        increment = builder.increment;
        cycle = builder.cycle;
        nextCPU = new AtomicInteger(firstCPU);
    }

    public void bindCurrentThreadToNextCPU() {
        ThreadBind.bindCurrentThreadToCPU(nextCPU.getAndUpdate(updateFunction));
    }

    private final IntUnaryOperator updateFunction = new IntUnaryOperator() {
        @Override
        public int applyAsInt(int cpu) {
            int next = cpu + increment;
            if (next <= lastCPU) {
                return next;
            }
            if (cycle) {
                return firstCPU;
            }
            throw new ThreadBindException("No more CPUs are available to bind to.");
        }
    };

    public static class Builder {
        private int firstCPU = -1;
        private int increment = -1;
        private int lastCPU = -1;
        private boolean cycle;

        public Builder firstCPU(int firstCPU) {
            Assume.notNegative(firstCPU);
            this.firstCPU = firstCPU;
            return this;
        }

        public Builder fromFirstCPU() {
            firstCPU(0);
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

        public Builder lastCPU(int lastCPU) {
            Assume.notNegative(lastCPU);
            this.lastCPU = lastCPU;
            return this;
        }

        public Builder toLastCPU() {
            lastCPU(Runtime.getRuntime().availableProcessors() - 1);
            return this;
        }

        public Builder cycle() {
            cycle = true;
            return this;
        }

        public ThreadSpreader build() {
            if (firstCPU == -1) {
                throw new IllegalStateException("firstCPU was not set.");
            }
            if (increment == -1) {
                throw new IllegalStateException("increment was not set.");
            }
            if (lastCPU == -1) {
                throw new IllegalStateException("lastCPU was not set.");
            }
            Ensure.notGreater(firstCPU, lastCPU);
            return new ThreadSpreader(this);
        }
    }
}
