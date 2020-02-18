package eu.menzani.ringbuffer;

import java.util.concurrent.locks.LockSupport;

public class SleepBusyWaitStrategy extends CompoundBusyWaitStrategy {
    private final long sleepTime;

    public SleepBusyWaitStrategy() {
        this(1L);
    }

    public SleepBusyWaitStrategy(long sleepTime) {
        this(sleepTime, 100);
    }

    public SleepBusyWaitStrategy(long sleepTime, int initialStrategyTicks) {
        this(sleepTime, new YieldBusyWaitStrategy(), initialStrategyTicks);
    }

    public SleepBusyWaitStrategy(long sleepTime, BusyWaitStrategy initialStrategy, int initialStrategyTicks) {
        super(initialStrategy, initialStrategyTicks);
        this.sleepTime = sleepTime;
    }

    @Override
    protected void doTick() {
        LockSupport.parkNanos(sleepTime);
    }
}
