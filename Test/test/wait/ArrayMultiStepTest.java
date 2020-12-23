package test.wait;

import org.ringbuffer.wait.ArrayMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class ArrayMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new ArrayMultiStepTest(true).runBenchmark();
    }

    public ArrayMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new ArrayMultiStepBusyWaitStrategy.Builder();
    }
}
