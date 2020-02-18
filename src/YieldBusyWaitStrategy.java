package eu.menzani.ringbuffer;

public class YieldBusyWaitStrategy extends CompoundBusyWaitStrategy {
    public YieldBusyWaitStrategy() {
        this(100);
    }

    public YieldBusyWaitStrategy(int initialStrategyTicks) {
        this(new HintBusyWaitStrategy(), initialStrategyTicks);
    }

    public YieldBusyWaitStrategy(BusyWaitStrategy initialStrategy, int initialStrategyTicks) {
        super(initialStrategy, initialStrategyTicks);
    }

    @Override
    protected void doTick() {
        Thread.yield();
    }
}
