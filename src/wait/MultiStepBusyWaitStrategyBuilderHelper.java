package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assume;

class MultiStepBusyWaitStrategyBuilderHelper {
    static void throwNoIntermediateStepsAdded() {
        throw new IllegalStateException("No intermediate steps were added.");
    }

    static void validateStrategyTicks(int strategyTicks) {
        Assume.notLesser(strategyTicks, 1);
    }
}
