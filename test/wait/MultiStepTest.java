package test.wait;

import eu.menzani.ringbuffer.wait.ArrayMultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class MultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new MultiStepTest().runBenchmark();
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new ArrayMultiStepBusyWaitStrategy.Builder();
    }
}
