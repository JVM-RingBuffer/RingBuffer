package test.wait;

import eu.menzani.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategyBuilder;

public class LinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new LinkedMultiStepTest().runBenchmark();
    }

    @Override
    MultiStepBusyWaitStrategyBuilder getStrategyBuilder() {
        return new LinkedMultiStepBusyWaitStrategy.Builder();
    }
}
