package org.ringbuffer.wait;

import org.ringbuffer.clock.BusyWaitClock;

public class BusyWaitClockBusyWaitStrategy implements BusyWaitStrategy {
    public static final BusyWaitClockBusyWaitStrategy DEFAULT_INSTANCE = new BusyWaitClockBusyWaitStrategy(50_000);

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    public static BusyWaitStrategy getDefault(int nanoseconds) {
        return WaitBusyWaitStrategy.createDefault(new BusyWaitClockBusyWaitStrategy(nanoseconds));
    }

    private final int nanoseconds;

    public BusyWaitClockBusyWaitStrategy(int nanoseconds) {
        this.nanoseconds = nanoseconds;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        BusyWaitClock.sleepCurrentThread(nanoseconds);
    }
}
