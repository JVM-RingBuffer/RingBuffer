package eu.menzani.ringbuffer.wait;

import java.util.*;

public class MultiStepBusyWaitStrategy implements BusyWaitStrategy {
    private final Node initialStrategy;
    private Node currentStrategy;
    private int counter;

    public static Builder endWith(BusyWaitStrategy finalStrategy) {
        return new Builder(finalStrategy);
    }

    private MultiStepBusyWaitStrategy(Builder builder) {
        initialStrategy = builder.getInitialStrategy();
    }

    @Override
    public void reset() {
        initialStrategy.strategy.reset();
        currentStrategy = initialStrategy;
        counter = 0;
    }

    @Override
    public void tick() {
        if (currentStrategy.next != null) {
            if (counter == currentStrategy.strategyTicks) {
                currentStrategy = currentStrategy.next;
                currentStrategy.strategy.reset();
                counter = 1;
            } else {
                counter++;
            }
        }
        currentStrategy.strategy.tick();
    }

    public static class Builder {
        private final BusyWaitStrategy finalStrategy;
        private final List<BusyWaitStrategy> strategies = new ArrayList<>();
        private final List<Integer> strategiesTicks = new ArrayList<>();

        private Builder(BusyWaitStrategy finalStrategy) {
            this.finalStrategy = finalStrategy;
        }

        public Builder after(BusyWaitStrategy strategy, int strategyTicks) {
            if (strategyTicks < 1) {
                throw new IllegalArgumentException("strategyTicks must be at least 1, but is " + strategyTicks);
            }
            if (strategy instanceof MultiStepBusyWaitStrategy) {
                Node node = ((MultiStepBusyWaitStrategy) strategy).initialStrategy;
                Deque<BusyWaitStrategy> strategies = new ArrayDeque<>();
                Deque<Integer> strategiesTicks = new ArrayDeque<>();
                do {
                    strategies.addFirst(node.strategy);
                    if (node.strategyTicks == 0) {
                        strategiesTicks.addFirst(strategyTicks);
                    } else {
                        strategiesTicks.addFirst(node.strategyTicks);
                    }
                    node = node.next;
                } while (node != null);
                this.strategies.addAll(strategies);
                this.strategiesTicks.addAll(strategiesTicks);
            } else {
                strategies.add(strategy);
                strategiesTicks.add(strategyTicks);
            }
            return this;
        }

        public BusyWaitStrategy build() {
            assert strategies.size() == strategiesTicks.size();
            if (strategies.isEmpty()) {
                throw new IllegalStateException("No steps added.");
            }
            return new MultiStepBusyWaitStrategy(this);
        }

        private Node getInitialStrategy() {
            Iterator<BusyWaitStrategy> strategies = this.strategies.iterator();
            Iterator<Integer> strategiesTicks = this.strategiesTicks.iterator();
            Node initialStrategy = new Node(strategies.next(), strategiesTicks.next(), new Node(finalStrategy, 0, null));
            while (strategies.hasNext()) {
                initialStrategy = new Node(strategies.next(), strategiesTicks.next(), initialStrategy);
            }
            return initialStrategy;
        }
    }

    private static class Node {
        final BusyWaitStrategy strategy;
        final int strategyTicks;
        final Node next;

        private Node(BusyWaitStrategy strategy, int strategyTicks, Node next) {
            this.strategy = strategy;
            this.strategyTicks = strategyTicks;
            this.next = next;
        }
    }
}
