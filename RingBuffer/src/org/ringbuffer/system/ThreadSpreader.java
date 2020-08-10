/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.system;

import org.ringbuffer.java.Assume;
import org.ringbuffer.java.Ensure;

import java.util.concurrent.atomic.AtomicInteger;

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

    public int bindCurrentThreadToNextCPU() {
        int cpu = nextCPU();
        Threads.bindCurrentThreadToCPU(cpu);
        return cpu;
    }

    int nextCPU() {
        return nextCPU.getAndUpdate(cpu -> {
            if (cpu > lastCPU) {
                throw new ThreadManipulationException("No more CPUs are available to bind to.");
            }
            int next = cpu + increment;
            if (cycle && next > lastCPU) {
                return firstCPU;
            }
            return next;
        });
    }

    public void reset() {
        nextCPU.set(firstCPU);
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
            toCPU(Runtime.getRuntime().availableProcessors());
            return this;
        }

        public Builder cycle() {
            cycle = true;
            return this;
        }

        public ThreadSpreader build() {
            if (firstCPU == -1) {
                throw new IllegalStateException("You must call fromCPU() or fromFirstCPU().");
            }
            if (increment == -1) {
                throw new IllegalStateException("You must call increment(), skipHyperthreads() or skipFourWayHyperthreads().");
            }
            if (lastCPU == -1) {
                throw new IllegalStateException("You must call toCPU() or toLastCPU().");
            }
            Ensure.notGreaterThan(firstCPU, lastCPU);
            return new ThreadSpreader(this);
        }
    }
}
