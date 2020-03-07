package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assert;
import eu.menzani.ringbuffer.java.Assume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiStepBusyWaitStrategy implements BusyWaitStrategy {
    private final BusyWaitStrategy[] strategies;
    private final int[] strategiesTicks;

    private BusyWaitStrategy currentStrategy;
    private int currentStrategyIndex;
    private int counter;

    public static MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder().endWith(finalStrategy);
    }

    private MultiStepBusyWaitStrategy(Builder builder) {
        strategies = builder.getStrategies();
        strategiesTicks = builder.getStrategiesTicks();
    }

    @Override
    public void reset() {
        currentStrategyIndex = strategies.length - 1;
        currentStrategy = strategies[currentStrategyIndex];
        currentStrategy.reset();
        counter = strategiesTicks[currentStrategyIndex];
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

    public static class Builder implements MultiStepBusyWaitStrategyBuilder {
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
            Assume.notLesser(strategyTicks, 1, "strategyTicks");
            if (strategy instanceof MultiStepBusyWaitStrategy) {
                MultiStepBusyWaitStrategy multiStepStrategy = (MultiStepBusyWaitStrategy) strategy;
                Collections.addAll(strategies, multiStepStrategy.strategies);
                strategiesTicks.add(strategyTicks);
                for (int i = 1, length = multiStepStrategy.strategiesTicks.length; i < length; i++) {
                    int multiStepStrategyTicks = multiStepStrategy.strategiesTicks[i];
                    if (i != length - 1) {
                        multiStepStrategyTicks++;
                    }
                    strategiesTicks.add(multiStepStrategyTicks);
                }
            } else {
                strategies.add(strategy);
                strategiesTicks.add(strategyTicks);
            }
            return this;
        }

        @Override
        public BusyWaitStrategy build() {
            Assert.equal(strategies.size(), strategiesTicks.size());
            if (strategies.size() == 1) {
                throw new IllegalStateException("No steps added.");
            }
            for (int i = 0; i < strategiesTicks.size() - 1; i++) {
                strategiesTicks.set(i, strategiesTicks.get(i) - 1);
            }
            return new MultiStepBusyWaitStrategy(this);
        }

        private BusyWaitStrategy[] getStrategies() {
            return strategies.toArray(new BusyWaitStrategy[0]);
        }

        private int[] getStrategiesTicks() {
            return strategiesTicks.stream().mapToInt(Integer::intValue).toArray();
        }
    }
}
