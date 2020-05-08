package test.wait;

import eu.menzani.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;
import eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class LinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new LinkedMultiStepTest(true).run();
    }

    public LinkedMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new LinkedMultiStepBusyWaitStrategy.Builder();
    }
}
