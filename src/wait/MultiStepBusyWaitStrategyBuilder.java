package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assume;

public interface MultiStepBusyWaitStrategyBuilder {
    MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy);

    MultiStepBusyWaitStrategyBuilder after(BusyWaitStrategy strategy, int strategyTicks);

    BusyWaitStrategy build();

    static void throwNoIntermediateStepsAdded() {
        throw new IllegalStateException("No intermediate steps were added.");
    }

    static void validateStrategyTicks(int strategyTicks) {
        Assume.notLesser(strategyTicks, 1);
    }
}
