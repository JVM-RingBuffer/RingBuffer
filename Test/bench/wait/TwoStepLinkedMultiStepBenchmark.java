package bench.wait;

import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;

public class TwoStepLinkedMultiStepBenchmark extends MultiStepBusyWaitStrategyBenchmark {
    public static void main(String[] args) {
        new TwoStepLinkedMultiStepBenchmark().runBenchmark();
    }

    public TwoStepLinkedMultiStepBenchmark() {
        this(true);
    }

    public TwoStepLinkedMultiStepBenchmark(boolean isBenchmark) {
        super(isBenchmark);
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return LinkedMultiStepBusyWaitStrategy.endWith(second)
                .after(first, STEP_TICKS)
                .build();
    }

    @Override
    public int getNumSteps() {
        return 2;
    }
}
