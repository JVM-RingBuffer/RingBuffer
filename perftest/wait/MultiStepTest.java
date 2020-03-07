package perftest.wait;

import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategyBuilder;

public class MultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new MultiStepTest().runBenchmark();
        new TwoStepMultiStepTest().runBenchmark();
    }

    @Override
    MultiStepBusyWaitStrategyBuilder getStrategyBuilder() {
        return new MultiStepBusyWaitStrategy.Builder();
    }
}
