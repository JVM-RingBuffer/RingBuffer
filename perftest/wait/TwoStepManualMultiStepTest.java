package perftest.wait;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class TwoStepManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
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
            FIRST.reset();
            counter = STEP_TICKS;
        }

        @Override
        public void tick() {
            switch (counter) {
                case 0:
                    SECOND.reset();
                    counter--;
                case -1:
                    SECOND.tick();
                    break;
                default:
                    FIRST.tick();
                    counter--;
            }
        }
    }
}
