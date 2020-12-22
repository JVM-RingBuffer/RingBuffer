/*
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

package test.wait;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.Profiler;
import eu.menzani.system.Threads;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;
import org.ringbuffer.wait.NoopBusyWaitStrategy;

public abstract class MultiStepBusyWaitStrategyTest extends Benchmark {
    public static final int STEP_TICKS = 100;
    public static final int NUM_TICKS = STEP_TICKS * (6 + 1);

    public static BusyWaitStrategy FIRST;
    public static BusyWaitStrategy SECOND;
    public static BusyWaitStrategy THIRD;
    public static BusyWaitStrategy FOURTH;
    public static BusyWaitStrategy FIFTH;
    public static BusyWaitStrategy SIXTH;

    private final boolean isPerfTest;

    MultiStepBusyWaitStrategyTest(boolean isPerfTest) {
        this.isPerfTest = isPerfTest;
        if (isPerfTest) {
            FIRST = SECOND = THIRD = FOURTH = FIFTH = SIXTH = NoopBusyWaitStrategy.DEFAULT_INSTANCE;
        }
    }

    public int getNumSteps() {
        return 6;
    }

    @Override
    public int getNumIterations() {
        return isPerfTest ? 300_000 : 2;
    }

    @Override
    public void runBenchmark() {
        if (isPerfTest) {
            Threads.bindCurrentThreadToCPU(2);
            Threads.setCurrentThreadPriorityToRealtime();
            super.runBenchmark();
        } else {
            test(getNumIterations());
        }
    }

    @Override
    protected void test(int i) {
        BusyWaitStrategy strategy = getStrategy();
        Profiler profiler = new Profiler(this, i);
        profiler.start();
        for (; i > 0; i--) {
            strategy.reset();
            for (int j = 0; j < NUM_TICKS; j++) {
                strategy.tick();
            }
        }
        profiler.stop();
    }

    BusyWaitStrategy getStrategy() {
        return getStrategyBuilder().endWith(SIXTH)
                .after(FIFTH, STEP_TICKS)
                .after(FOURTH, STEP_TICKS)
                .after(getStrategyBuilder().endWith(THIRD)
                        .after(SECOND, STEP_TICKS)
                        .after(FIRST, STEP_TICKS)
                        .build(), STEP_TICKS)
                .build();
    }

    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        throw new AssertionError();
    }
}
