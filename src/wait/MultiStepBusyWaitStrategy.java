package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiStepBusyWaitStrategy implements BusyWaitStrategy {
    private final int initialStrategyIndex;
    private final BusyWaitStrategy[] strategies;
    private final int[] strategiesTicks;

    private BusyWaitStrategy currentStrategy;
    private int currentStrategyIndex;
    private int counter;

    public static MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder().endWith(finalStrategy);
    }

    private MultiStepBusyWaitStrategy(Builder builder) {
        initialStrategyIndex = builder.getInitialStrategyIndex();
        strategies = builder.getStrategies();
        strategiesTicks = builder.getStrategiesTicks();
    }

    @Override
    public void reset() {
        currentStrategyIndex = initialStrategyIndex;
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

    public static class Builder extends AbstractMultiStepBusyWaitStrategyBuilder {
        private final List<BusyWaitStrategy> strategies = new ArrayList<>();
        private final List<Integer> strategiesTicks = new ArrayList<>();

        @Override
        public MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy) {
            strategies.add(finalStrategy);
            strategiesTicks.add(0);
            return this;
        }

        @Override
        public MultiStepBusyWaitStrategyBuilder after(BusyWaitStrategy strategy, int strategyTicks) {
            validateStrategyTicks(strategyTicks);
            strategiesTicks.add(strategyTicks - 1);
            if (strategy instanceof MultiStepBusyWaitStrategy) {
                MultiStepBusyWaitStrategy multiStepStrategy = (MultiStepBusyWaitStrategy) strategy;
                Collections.addAll(strategies, multiStepStrategy.strategies);
                for (int i = 1; i < multiStepStrategy.strategiesTicks.length; i++) {
                    strategiesTicks.add(multiStepStrategy.strategiesTicks[i]);
                }
            } else {
                strategies.add(strategy);
            }
            return this;
        }

        @Override
        public BusyWaitStrategy build() {
            Assert.equal(strategies.size(), strategiesTicks.size());
            if (strategies.size() == 1) {
                throwNoIntermediateStepsAdded();
            }
            return new MultiStepBusyWaitStrategy(this);
        }

        private int getInitialStrategyIndex() {
            return strategies.size();
        }

        private BusyWaitStrategy[] getStrategies() {
            return strategies.toArray(new BusyWaitStrategy[0]);
        }

        private int[] getStrategiesTicks() {
            return strategiesTicks.stream().mapToInt(Integer::intValue).toArray();
        }
    }
}
