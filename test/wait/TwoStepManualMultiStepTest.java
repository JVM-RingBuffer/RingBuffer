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
