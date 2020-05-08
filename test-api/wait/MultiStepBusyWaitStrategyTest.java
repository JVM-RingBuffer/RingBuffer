package test.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.NoopBusyWaitStrategy;
import test.Benchmark;
import test.Profiler;

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
    protected int getWarmupRepeatTimes() {
        return isPerfTest ? getRepeatTimes() : 0;
    }

    @Override
    protected int getRepeatTimes() {
        return isPerfTest ? 50 : 1;
    }

    @Override
    public int getNumIterations() {
        return isPerfTest ? 300_000 : 2;
    }

    @Override
    protected void test(int i) {
        BusyWaitStrategy strategy = getStrategy();
        Profiler profiler = newProfiler();
        profiler.start();
        for (; i > 0; i--) {
            strategy.reset();
            for (int j = 0; j < NUM_TICKS; j++) {
                strategy.tick();
            }
        }
        profiler.stop();
        add(profiler);
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
