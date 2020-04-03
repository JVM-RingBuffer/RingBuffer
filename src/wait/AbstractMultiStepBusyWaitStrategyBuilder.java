package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assume;

public abstract class AbstractMultiStepBusyWaitStrategyBuilder implements MultiStepBusyWaitStrategyBuilder {
    public static void throwNoIntermediateStepsAdded() {
        throw new IllegalStateException("No intermediate steps were added.");
    }

    public static void validateStrategyTicks(int strategyTicks) {
        Assume.notLesser(strategyTicks, 1, "strategyTicks");
    }
}
