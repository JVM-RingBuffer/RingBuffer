package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.java.Assert;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Might be slightly faster than {@link MultiStepBusyWaitStrategy} for two total steps, and slower otherwise.
 */
public class LinkedMultiStepBusyWaitStrategy implements BusyWaitStrategy {
    private final Node initialStrategy;
    private Node currentStrategy;
    private int counter;

    public static MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder().endWith(finalStrategy);
    }

    private LinkedMultiStepBusyWaitStrategy(Builder builder) {
        initialStrategy = builder.getInitialStrategy();
    }

    @Override
    public void reset() {
        currentStrategy = initialStrategy;
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

    public static class Builder implements MultiStepBusyWaitStrategyBuilder {
        private BusyWaitStrategy finalStrategy;
        private final List<BusyWaitStrategy> strategies = new ArrayList<>();
        private final List<Integer> strategiesTicks = new ArrayList<>();

        @Override
        public MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy) {
            this.finalStrategy = finalStrategy;
            return this;
        }

        @Override
        public MultiStepBusyWaitStrategyBuilder after(BusyWaitStrategy strategy, int strategyTicks) {
            MultiStepBusyWaitStrategyBuilder.validateStrategyTicks(strategyTicks);
            if (strategy instanceof LinkedMultiStepBusyWaitStrategy) {
                Node node = ((LinkedMultiStepBusyWaitStrategy) strategy).initialStrategy.next;
                Deque<BusyWaitStrategy> strategies = new ArrayDeque<>();
                Deque<Integer> strategiesTicks = new ArrayDeque<>();
                do {
                    strategies.addFirst(node.strategy);
                    if (node.strategyTicks == 0) {
                        strategiesTicks.addFirst(strategyTicks - 1);
                    } else {
                        strategiesTicks.addFirst(node.strategyTicks);
                    }
                    node = node.next;
                } while (node != null);
                this.strategies.addAll(strategies);
                this.strategiesTicks.addAll(strategiesTicks);
            } else {
                strategies.add(strategy);
                strategiesTicks.add(strategyTicks - 1);
            }
            return this;
        }

        @Override
        public BusyWaitStrategy build() {
            Assert.equal(strategies.size(), strategiesTicks.size());
            if (strategies.isEmpty()) {
                MultiStepBusyWaitStrategyBuilder.throwNoIntermediateStepsAdded();
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
