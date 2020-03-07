package perftest.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategyBuilder;
import eu.menzani.ringbuffer.wait.NoopBusyWaitStrategy;
import perftest.Benchmark;
import perftest.Profiler;

public abstract class MultiStepBusyWaitStrategyTest {
    public static final int STEP_TICKS = 100;
    public static final int NUM_TICKS = STEP_TICKS * (6 + 1);

    public static BusyWaitStrategy FIRST;
    public static BusyWaitStrategy SECOND;
    public static BusyWaitStrategy THIRD;
    public static BusyWaitStrategy FOURTH;
    public static BusyWaitStrategy FIFTH;
    public static BusyWaitStrategy SIXTH;

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

    MultiStepBusyWaitStrategyBuilder getStrategyBuilder() {
        throw new AssertionError();
    }

    public int getNumSteps() {
        return 6;
    }

    void runBenchmark() {
        FIRST = SECOND = THIRD = FOURTH = FIFTH = SIXTH = new NoopBusyWaitStrategy();

        final int numIterations = 10_000;
        final int repeatTimes = 1_000;
        run(numIterations, repeatTimes);
        Benchmark.begin();
        run(numIterations, repeatTimes);
        Benchmark.report();
    }

    public void run(int numIterations, int repeatTimes) {
        Profiler profiler = new Profiler(numIterations);
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
            Benchmark.add(profiler);
        }
    }
}
