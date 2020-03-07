package perftest.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new ManualMultiStepTest().runBenchmark();
        new TwoStepManualMultiStepTest().runBenchmark();
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return new ManualMultiStepBusyWaitStrategy();
    }

    private static class ManualMultiStepBusyWaitStrategy implements BusyWaitStrategy {
        private int counter;
        private int step;

        @Override
        public void reset() {
            counter = STEP_TICKS - 1;
            step = 6;
        }

        @Override
        public void tick() {
            switch (step) {
                case 1:
                    countDown(SIXTH);
                case 0:
                    SIXTH.tick();
                    break;
                case 2:
                    countDown(FIFTH);
                    FIFTH.tick();
                    break;
                case 3:
                    countDown(FOURTH);
                    FOURTH.tick();
                    break;
                case 4:
                    countDown(THIRD);
                    THIRD.tick();
                    break;
                case 5:
                    countDown(SECOND);
                    SECOND.tick();
                    break;
                case 6:
                    countDown(FIRST);
                    FIRST.tick();
            }
        }

        private void countDown(BusyWaitStrategy currentStrategy) {
            switch (counter) {
                case 0:
                    step--;
                    counter = STEP_TICKS - 1;
                    break;
                case STEP_TICKS - 1:
                    currentStrategy.reset();
                default:
                    counter--;
            }
        }
    }
}
