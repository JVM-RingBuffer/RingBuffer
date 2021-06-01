package bench.wait;

import org.ringbuffer.wait.ArrayMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.BusyWaitStrategy;

public class TwoStepArrayMultiStepBenchmark extends MultiStepBusyWaitStrategyBenchmark {
    public static void main(String[] args) {
        new TwoStepArrayMultiStepBenchmark().runBenchmark();
    }

    public TwoStepArrayMultiStepBenchmark() {
        this(true);
    }

    public TwoStepArrayMultiStepBenchmark(boolean isBenchmark) {
        super(isBenchmark);
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return ArrayMultiStepBusyWaitStrategy.endWith(second)
                .after(first, STEP_TICKS)
                .build();
    }

    @Override
    public int getNumSteps() {
        return 2;
    }
}
