package org.ringbuffer.wait;

import eu.menzani.lang.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.ringbuffer.wait.MultiStepBusyWaitStrategyBuilderHelper.throwNoIntermediateStepsAdded;
import static org.ringbuffer.wait.MultiStepBusyWaitStrategyBuilderHelper.validateStrategyTicks;

/**
 * Might be slightly faster than {@link ArrayMultiStepBusyWaitStrategy} for two total steps, and slower otherwise.
 */
public class LinkedMultiStepBusyWaitStrategy implements MultiStepBusyWaitStrategy {
    private final Node initialStrategy;
    private Node currentStrategy;
    private int counter;

    public static MultiStepBusyWaitStrategy.Builder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder().endWith(finalStrategy);
    }

    LinkedMultiStepBusyWaitStrategy(Builder builder) {
        initialStrategy = builder.getInitialStrategy();
    }

    @Override
    public void reset() {
        currentStrategy = initialStrategy;
        counter = 0;
    }

    @Override
    public void tick() {
        if (currentStrategy.next != null) {
            if (counter == 0) {
                currentStrategy = currentStrategy.next;
                currentStrategy.strategy.reset();
                counter = currentStrategy.strategyTicks;
            } else {
                counter--;
            }
        }
        currentStrategy.strategy.tick();
    }

    @Override
    public List<BusyWaitStrategy> getStrategies() {
        List<BusyWaitStrategy> strategies = new ArrayList<>(5);
        Node node = initialStrategy.next;
        do {
            strategies.add(node.strategy);
            node = node.next;
        } while (node != null);
        Collections.reverse(strategies);
        return strategies;
    }

    @Override
    public List<Integer> getStrategiesTicks() {
        List<Integer> strategiesTicks = new ArrayList<>(5);
        Node node = initialStrategy.next;
        do {
            strategiesTicks.add(node.strategyTicks);
            node = node.next;
        } while (node != null && node.strategyTicks != 0);
        Collections.reverse(strategiesTicks);
        return strategiesTicks;
    }

    public static class Builder implements MultiStepBusyWaitStrategy.Builder {
        private BusyWaitStrategy finalStrategy;
        private final List<BusyWaitStrategy> strategies = new ArrayList<>();
        private final List<Integer> strategiesTicks = new ArrayList<>();

        @Override
        public MultiStepBusyWaitStrategy.Builder endWith(BusyWaitStrategy finalStrategy) {
            if (finalStrategy instanceof MultiStepBusyWaitStrategy) {
                MultiStepBusyWaitStrategy finalMultiStepStrategy = (MultiStepBusyWaitStrategy) finalStrategy;
                List<BusyWaitStrategy> strategies = finalMultiStepStrategy.getStrategies();
                int lastIndex = strategies.size() - 1;
                this.strategies.addAll(strategies.subList(0, lastIndex));
                this.finalStrategy = strategies.get(lastIndex);
                strategiesTicks.addAll(finalMultiStepStrategy.getStrategiesTicks());
            } else {
                this.finalStrategy = finalStrategy;
            }
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
            if (strategies.isEmpty()) {
                throwNoIntermediateStepsAdded();
            }
            return new LinkedMultiStepBusyWaitStrategy(this);
        }

        private Node getInitialStrategy() {
            Iterator<BusyWaitStrategy> strategies = this.strategies.iterator();
            Iterator<Integer> strategiesTicks = this.strategiesTicks.iterator();
            Node initialStrategy = new Node(strategies.next(), strategiesTicks.next(), new Node(finalStrategy, 0, null));
            while (strategies.hasNext()) {
                initialStrategy = new Node(strategies.next(), strategiesTicks.next(), initialStrategy);
            }
            return new Node(null, 0, initialStrategy);
        }
    }

    private static class Node {
        final BusyWaitStrategy strategy;
        final int strategyTicks;
        final Node next;

        Node(BusyWaitStrategy strategy, int strategyTicks, Node next) {
            this.strategy = strategy;
            this.strategyTicks = strategyTicks;
            this.next = next;
        }
    }
}
