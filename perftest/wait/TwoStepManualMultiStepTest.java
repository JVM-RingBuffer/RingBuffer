package test.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class TwoStepManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new TwoStepManualMultiStepTest(true).runBenchmark();
    }

    public TwoStepManualMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return new TwoStepManualMultiStepBusyWaitStrategy();
    }

    @Override
    public int getNumSteps() {
        return 2;
    }

    private static class TwoStepManualMultiStepBusyWaitStrategy implements BusyWaitStrategy {
        private int counter;

        @Override
        public void reset() {
            counter = STEP_TICKS + 1;
        }

        @Override
        public void tick() {
            switch (counter) {
                case 0:
                    SECOND.tick();
                    return;
                case 1:
                    SECOND.reset();
                    SECOND.tick();
                    break;
                case STEP_TICKS + 1:
                    FIRST.reset();
                default:
                    FIRST.tick();
            }
            counter--;
        }
    }
}
