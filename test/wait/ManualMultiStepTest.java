package test.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new ManualMultiStepTest(true).run();
    }

    public ManualMultiStepTest(boolean isPerfTest) {
        super(isPerfTest);
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
                case 0:
                    SIXTH.tick();
                    break;
                case 1:
                    countDown(SIXTH);
                    break;
                case 2:
                    countDown(FIFTH);
                    break;
                case 3:
                    countDown(FOURTH);
                    break;
                case 4:
                    countDown(THIRD);
                    break;
                case 5:
                    countDown(SECOND);
                    break;
                case 6:
                    countDown(FIRST);
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
            currentStrategy.tick();
        }
    }
}
