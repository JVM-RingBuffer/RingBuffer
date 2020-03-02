package eu.menzani.ringbuffer.wait;

import java.util.concurrent.locks.LockSupport;

public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    public static BusyWaitStrategy getDefault() {
        return MultiStepBusyWaitStrategy.endWith(new SleepBusyWaitStrategy())
                .after(YieldBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    private final long sleepTime;

    public SleepBusyWaitStrategy() {
        this(1L);
    }

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
