package test.wait;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.system.ThreadManipulation;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;
import org.ringbuffer.wait.NoopBusyWaitStrategy;

public abstract class MultiStepBusyWaitStrategyTest extends Benchmark {
    public static final int STEP_TICKS = 100;
    public static final int NUM_TICKS = STEP_TICKS * (6 + 1);

    public BusyWaitStrategy first;
    public BusyWaitStrategy second;
    public BusyWaitStrategy third;
    public BusyWaitStrategy fourth;
    public BusyWaitStrategy fifth;
    public BusyWaitStrategy sixth;

    private final boolean isPerfTest;
    private BusyWaitStrategy strategy;

    MultiStepBusyWaitStrategyTest(boolean isPerfTest) {
        this.isPerfTest = isPerfTest;
        if (isPerfTest) {
            first = second = third = fourth = fifth = sixth = NoopBusyWaitStrategy.DEFAULT_INSTANCE;
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
    protected ThreadManipulation getThreadManipulation() {
        if (isPerfTest) {
            return super.getThreadManipulation();
        }
        return ThreadManipulation.doNothing();
    }

    @Override
    protected boolean shouldAutoProfile() {
        return isPerfTest;
    }

    @Override
    public void runBenchmark() {
        strategy = getStrategy();
        if (isPerfTest) {
            super.runBenchmark();
        } else {
            test(getNumIterations());
        }
    }

    @Override
    protected void test(int i) {
        BusyWaitStrategy strategy = this.strategy;
        for (; i > 0; i--) {
            strategy.reset();
            for (int j = 0; j < NUM_TICKS; j++) {
                strategy.tick();
            }
        }
    }

    BusyWaitStrategy getStrategy() {
        return getStrategyBuilder().endWith(sixth)
                .after(fifth, STEP_TICKS)
                .after(fourth, STEP_TICKS)
                .after(getStrategyBuilder().endWith(third)
                        .after(second, STEP_TICKS)
                        .after(first, STEP_TICKS)
                        .build(), STEP_TICKS)
                .build();
    }

    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        throw new AssertionError();
    }
}
