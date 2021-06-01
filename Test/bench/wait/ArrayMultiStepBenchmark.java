package bench.wait;

import org.ringbuffer.wait.ArrayMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.MultiStepBusyWaitStrategy;

public class ArrayMultiStepBenchmark extends MultiStepBusyWaitStrategyBenchmark {
    public static void main(String[] args) {
        new ArrayMultiStepBenchmark().runBenchmark();
    }

    public ArrayMultiStepBenchmark() {
        this(true);
    }

    public ArrayMultiStepBenchmark(boolean isBenchmark) {
        super(isBenchmark);
    }

    @Override
    MultiStepBusyWaitStrategy.Builder getStrategyBuilder() {
        return new ArrayMultiStepBusyWaitStrategy.Builder();
    }
}
