package test.wait;

import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.LinkedMultiStepBusyWaitStrategy;

public class TwoStepLinkedMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new TwoStepLinkedMultiStepTest().runBenchmark();
    }

    public TwoStepLinkedMultiStepTest() {
        this(true);
    }

    public TwoStepLinkedMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
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
