package org.ringbuffer.wait;

import eu.menzani.system.LinuxSleep;

public class NanosleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final NanosleepBusyWaitStrategy DEFAULT_INSTANCE = new NanosleepBusyWaitStrategy(5);

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    public static BusyWaitStrategy getDefault(int nanoseconds) {
        return WaitBusyWaitStrategy.createDefault(new NanosleepBusyWaitStrategy(nanoseconds));
    }

    private final int nanoseconds;

    public NanosleepBusyWaitStrategy(int nanoseconds) {
        this.nanoseconds = nanoseconds;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        LinuxSleep.sleep(nanoseconds);
    }
}
