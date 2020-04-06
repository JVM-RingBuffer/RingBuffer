package eu.menzani.ringbuffer.wait;

import java.util.concurrent.locks.LockSupport;

/**
 * Requires Linux.
 */
public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final SleepBusyWaitStrategy DEFAULT_INSTANCE = new SleepBusyWaitStrategy(1L);

    public static BusyWaitStrategy getDefault() {
        return MultiStepBusyWaitStrategy.endWith(DEFAULT_INSTANCE)
                .after(YieldBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    private final long sleepTime;

    /**
     * @param sleepTime In nanoseconds.
     */
    public SleepBusyWaitStrategy(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void tick() {
        LockSupport.parkNanos(sleepTime);
    }
}
