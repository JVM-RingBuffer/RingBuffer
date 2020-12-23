package test.wait;

import org.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class LinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new LinkedMultiStepTest(true).runBenchmark();
    }

    public LinkedMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new LinkedMultiStepBusyWaitStrategy.Builder();
    }
}
