/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.wait;

import org.ringbuffer.wait.BusyWaitStrategy;

public class ManualMultiStepTest extends MultiStepBusyWaitStrategyTest {
    public static void main(String[] args) {
        new ManualMultiStepTest(true).runBenchmark();
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
