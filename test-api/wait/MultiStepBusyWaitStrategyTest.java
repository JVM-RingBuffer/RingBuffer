package test.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.NoopBusyWaitStrategy;
import test.Benchmark;
import test.Profiler;

public abstract class MultiStepBusyWaitStrategyTest {
    public static final int STEP_TICKS = 100;
    public static final int NUM_TICKS = STEP_TICKS * (6 + 1);

    public static BusyWaitStrategy FIRST;
    public static BusyWaitStrategy SECOND;
    public static BusyWaitStrategy THIRD;
    public static BusyWaitStrategy FOURTH;
    public static BusyWaitStrategy FIFTH;
    public static BusyWaitStrategy SIXTH;

    private final Benchmark benchmark = new Benchmark();

    public int getNumSteps() {
        return 6;
    }

    void runBenchmark() {
        FIRST = SECOND = THIRD = FOURTH = FIFTH = SIXTH = new NoopBusyWaitStrategy();

        final int numIterations = 300_000;
        final int repeatTimes = 50;
        run(numIterations, repeatTimes);
        benchmark.begin();
        run(numIterations, repeatTimes);
        benchmark.report();
    }

    public void run(int numIterations, int repeatTimes) {
        Profiler profiler = new Profiler(this, numIterations);
        BusyWaitStrategy strategy = getStrategy();
        for (int i = 0; i < repeatTimes; i++) {
            profiler.start();
            for (int j = 0; j < numIterations; j++) {
                strategy.reset();
                for (int k = 0; k < NUM_TICKS; k++) {
                    strategy.tick();
                }
            }
            profiler.stop();
            benchmark.add(profiler);
        }
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
