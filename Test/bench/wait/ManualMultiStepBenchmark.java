package bench.wait;

import org.ringbuffer.wait.BusyWaitStrategy;

public class ManualMultiStepBenchmark extends bench.wait.MultiStepBusyWaitStrategyBenchmark {
    public static void main(String[] args) {
        new ManualMultiStepBenchmark().runBenchmark();
    }

    public ManualMultiStepBenchmark() {
        this(true);
    }

    public ManualMultiStepBenchmark(boolean isBenchmark) {
        super(isBenchmark);
    }

    @Override
    BusyWaitStrategy getStrategy() {
        return new ManualMultiStepBusyWaitStrategy();
    }

    private class ManualMultiStepBusyWaitStrategy implements BusyWaitStrategy {
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
                    sixth.tick();
                    break;
                case 1:
                    countDown(sixth);
                    break;
                case 2:
                    countDown(fifth);
                    break;
                case 3:
                    countDown(fourth);
                    break;
                case 4:
                    countDown(third);
                    break;
                case 5:
                    countDown(second);
                    break;
                case 6:
                    countDown(first);
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
