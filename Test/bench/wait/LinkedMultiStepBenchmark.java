package bench.wait;

import org.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class LinkedMultiStepBenchmark extends MultiStepBusyWaitStrategyBenchmark {
    public static void main(String[] args) {
        new LinkedMultiStepBenchmark().runBenchmark();
    }

    public LinkedMultiStepBenchmark() {
        this(true);
    }

    public LinkedMultiStepBenchmark(boolean isBenchmark) {
        super(isBenchmark);
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new LinkedMultiStepBusyWaitStrategy.Builder();
    }
}
