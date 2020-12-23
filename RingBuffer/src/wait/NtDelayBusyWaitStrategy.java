package org.ringbuffer.wait;

import eu.menzani.system.WindowsSleep;

public class NtDelayBusyWaitStrategy implements BusyWaitStrategy {
    public static final NtDelayBusyWaitStrategy DEFAULT_INSTANCE = new NtDelayBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        WindowsSleep.sleepHalfAMillisecond();
    }
}
