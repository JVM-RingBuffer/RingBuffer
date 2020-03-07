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

        @Override
        public void reset() {
            FIRST.reset();
            counter = STEP_TICKS * 5;
        }

        @Override
        public void tick() {
            if (counter == -1) {
                SIXTH.tick();
            } else {
                switch (counter) {
                    case STEP_TICKS * 4:
                        SECOND.reset();
                        break;
                    case STEP_TICKS * 3:
                        THIRD.reset();
                        break;
                    case STEP_TICKS * 2:
                        FOURTH.reset();
                        break;
                    case STEP_TICKS:
                        FIFTH.reset();
                        break;
                    case 0:
                        SIXTH.reset();
                        SIXTH.tick();
                        counter--;
                        return;
                }
                if (--counter < STEP_TICKS) {
                    FIFTH.tick();
                } else if (counter < STEP_TICKS * 2) {
                    FOURTH.tick();
                } else if (counter < STEP_TICKS * 3) {
                    THIRD.tick();
                } else if (counter < STEP_TICKS * 4) {
                    SECOND.tick();
                } else {
                    FIRST.tick();
                }
            }
        }
    }
}
