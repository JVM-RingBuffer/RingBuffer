package perftest.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;

public class TwoStepLinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new TwoStepLinkedMultiStepTest().runBenchmark();
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return LinkedMultiStepBusyWaitStrategy.endWith(SECOND)
                .after(FIRST, STEP_TICKS)
                .build();
    }

    @Override
    public int getNumSteps() {
        return 2;
    }
}
