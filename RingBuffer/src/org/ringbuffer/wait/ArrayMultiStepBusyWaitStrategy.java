/*
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

package org.ringbuffer.wait;

import org.ringbuffer.lang.ArrayView;
import org.ringbuffer.lang.Assert;
import org.ringbuffer.lang.IntArrayView;

import java.util.ArrayList;
import java.util.List;

import static org.ringbuffer.wait.MultiStepBusyWaitStrategyBuilderHelper.throwNoIntermediateStepsAdded;
import static org.ringbuffer.wait.MultiStepBusyWaitStrategyBuilderHelper.validateStrategyTicks;

public class ArrayMultiStepBusyWaitStrategy implements MultiStepBusyWaitStrategy {
    private final int initialStrategyIndex;
    private final BusyWaitStrategy[] strategies;
    private final int[] strategiesTicks;

    private BusyWaitStrategy currentStrategy;
    private int currentStrategyIndex;
    private int counter;

    public static MultiStepBusyWaitStrategy.Builder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder().endWith(finalStrategy);
    }

    ArrayMultiStepBusyWaitStrategy(Builder builder) {
        initialStrategyIndex = builder.getInitialStrategyIndex();
        strategies = builder.getStrategies();
        strategiesTicks = builder.getStrategiesTicks();
    }

    @Override
    public void reset() {
        currentStrategyIndex = initialStrategyIndex;
        counter = 0;
    }

    @Override
    public void tick() {
        if (currentStrategyIndex != 0) {
            if (counter == 0) {
                currentStrategy = strategies[--currentStrategyIndex];
                currentStrategy.reset();
                counter = strategiesTicks[currentStrategyIndex];
            } else {
                counter--;
            }
        }
        currentStrategy.tick();
    }

    @Override
    public List<BusyWaitStrategy> getStrategies() {
        return new ArrayView<>(strategies);
    }

    @Override
    public List<Integer> getStrategiesTicks() {
        return new IntArrayView(strategiesTicks, 1);
    }

    public static class Builder implements MultiStepBusyWaitStrategy.Builder {
        private final List<BusyWaitStrategy> strategies = new ArrayList<>();
        private final List<Integer> strategiesTicks = new ArrayList<>();

        @Override
        public MultiStepBusyWaitStrategy.Builder endWith(BusyWaitStrategy finalStrategy) {
            if (finalStrategy instanceof MultiStepBusyWaitStrategy) {
                MultiStepBusyWaitStrategy finalMultiStepStrategy = (MultiStepBusyWaitStrategy) finalStrategy;
                strategies.addAll(finalMultiStepStrategy.getStrategies());
                strategiesTicks.addAll(finalMultiStepStrategy.getStrategiesTicks());
            } else {
                strategies.add(finalStrategy);
            }
            strategiesTicks.add(0);
            return this;
        }

        @Override
        public MultiStepBusyWaitStrategy.Builder after(BusyWaitStrategy strategy, int strategyTicks) {
            validateStrategyTicks(strategyTicks);
            if (strategy instanceof MultiStepBusyWaitStrategy) {
                MultiStepBusyWaitStrategy multiStepStrategy = (MultiStepBusyWaitStrategy) strategy;
                strategies.addAll(multiStepStrategy.getStrategies());
                strategiesTicks.addAll(multiStepStrategy.getStrategiesTicks());
            } else {
                strategies.add(strategy);
            }
            strategiesTicks.add(strategyTicks - 1);
            return this;
        }

        @Override
        public MultiStepBusyWaitStrategy build() {
            Assert.equal(strategies.size(), strategiesTicks.size());
            if (strategies.size() == 1) {
                throwNoIntermediateStepsAdded();
            }
            return new ArrayMultiStepBusyWaitStrategy(this);
        }

        int getInitialStrategyIndex() {
            return strategies.size();
        }

        BusyWaitStrategy[] getStrategies() {
            return strategies.toArray(new BusyWaitStrategy[0]);
        }

        int[] getStrategiesTicks() {
            int[] strategiesTicks = new int[this.strategiesTicks.size()];
            int i = 0;
            for (Integer strategyTicks : this.strategiesTicks) {
                strategiesTicks[i++] = strategyTicks;
            }
            Assert.equal(i, strategiesTicks.length);
            return strategiesTicks;
        }
    }
}
