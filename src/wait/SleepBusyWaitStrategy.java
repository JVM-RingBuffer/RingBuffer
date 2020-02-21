package eu.menzani.ringbuffer.wait;

import java.util.concurrent.locks.LockSupport;

public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    private static final SleepBusyWaitStrategy instance = new SleepBusyWaitStrategy(1L);

    public static BusyWaitStrategy getDefault() {
        return MultiStepBusyWaitStrategy.endWith(instance)
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
