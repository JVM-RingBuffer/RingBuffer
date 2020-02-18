package eu.menzani.ringbuffer.wait;

import java.util.concurrent.locks.LockSupport;

public class SleepBusyWaitStrategy extends CompoundBusyWaitStrategy {
    private final long sleepTime;

    public SleepBusyWaitStrategy() {
        this(1L);
    }

    /**
     * @param sleepTime In nanoseconds.
     */
    public SleepBusyWaitStrategy(long sleepTime) {
        this(sleepTime, 100);
    }

    /**
     * @param sleepTime In nanoseconds.
     */
    public SleepBusyWaitStrategy(long sleepTime, int initialStrategyTicks) {
        this(sleepTime, new YieldBusyWaitStrategy(), initialStrategyTicks);
    }

    /**
     * @param sleepTime In nanoseconds.
     */
    public SleepBusyWaitStrategy(long sleepTime, BusyWaitStrategy initialStrategy, int initialStrategyTicks) {
        super(initialStrategy, initialStrategyTicks);
        this.sleepTime = sleepTime;
    }

    @Override
    protected void doTick() {
        LockSupport.parkNanos(sleepTime);
    }
}
