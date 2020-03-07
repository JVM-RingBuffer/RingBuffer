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
            step = 5;
        }

        @Override
        public void tick() {
            switch (step) {
                case 0:
                    countDown(SIXTH);
                case -1:
                    SIXTH.tick();
                    break;
                case 1:
                    countDown(FIFTH);
                    FIFTH.tick();
                    break;
                case 2:
                    countDown(FOURTH);
                    FOURTH.tick();
                    break;
                case 3:
                    countDown(THIRD);
                    THIRD.tick();
                    break;
                case 4:
                    countDown(SECOND);
                    SECOND.tick();
                    break;
                case 5:
                    countDown(FIRST);
                    FIRST.tick();
                    break;
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
