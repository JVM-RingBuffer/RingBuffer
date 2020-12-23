package org.ringbuffer.wait;

import eu.menzani.lang.Assume;

class MultiStepBusyWaitStrategyBuilderHelper {
    static void throwNoIntermediateStepsAdded() {
        throw new IllegalStateException("No intermediate steps were added.");
    }

    static void validateStrategyTicks(int strategyTicks) {
        Assume.notLesser(strategyTicks, 1);
    }
}
