package eu.menzani.ringbuffer;

import java.util.concurrent.locks.LockSupport;

public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final SleepBusyWaitStrategy DEFAULT = new SleepBusyWaitStrategy(100L);

    private final long sleepTime;

    public SleepBusyWaitStrategy(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void tick() {
        LockSupport.parkNanos(sleepTime);
    }
}
