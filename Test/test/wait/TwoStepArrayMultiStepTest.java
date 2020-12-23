package test.wait;

import org.ringbuffer.wait.ArrayMultiStepBusyWaitStrategy;
import org.ringbuffer.wait.BusyWaitStrategy;

public class TwoStepArrayMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new TwoStepArrayMultiStepTest(true).runBenchmark();
    }

    public TwoStepArrayMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return ArrayMultiStepBusyWaitStrategy.endWith(SECOND)
                .after(FIRST, STEP_TICKS)
                .build();
    }

    @Override
    public int getNumSteps() {
        return 2;
    }
}
