package test.wait;

import eu.menzani.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class LinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new LinkedMultiStepTest(true).runBenchmark();
    }

    public LinkedMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    String getProfilerName() {
        return "LinkedMultiStep";
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new LinkedMultiStepBusyWaitStrategy.Builder();
    }
}
