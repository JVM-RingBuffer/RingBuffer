package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.ArrayView;
import eu.menzani.ringbuffer.java.Assert;
import eu.menzani.ringbuffer.java.IntArrayView;

import java.util.ArrayList;
import java.util.List;

import static eu.menzani.ringbuffer.wait.MultiStepBusyWaitStrategyBuilderHelper.*;

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

    private ArrayMultiStepBusyWaitStrategy(Builder builder) {
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

        private int getInitialStrategyIndex() {
            return strategies.size();
        }

        private BusyWaitStrategy[] getStrategies() {
            return strategies.toArray(new BusyWaitStrategy[0]);
        }

        private int[] getStrategiesTicks() {
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
