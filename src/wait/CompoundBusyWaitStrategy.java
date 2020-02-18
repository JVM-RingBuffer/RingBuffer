package eu.menzani.ringbuffer.wait;

public abstract class CompoundBusyWaitStrategy implements BusyWaitStrategy {
    private final BusyWaitStrategy initialStrategy;
    private final int initialStrategyTicks;
    private int counter;

    protected CompoundBusyWaitStrategy(BusyWaitStrategy initialStrategy, int initialStrategyTicks) {
        this.initialStrategy = initialStrategy;
        this.initialStrategyTicks = initialStrategyTicks;
    }

    @Override
    public void reset() {
        counter = initialStrategyTicks;
        initialStrategy.reset();
    }

    @Override
    public void tick() {
        if (counter == 0) {
            doTick();
        } else {
            counter--;
            initialStrategy.tick();
        }
    }

    protected abstract void doTick();
}
