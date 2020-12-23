package org.ringbuffer.wait;

import eu.menzani.system.Unsafe;

public class ParkBusyWaitStrategy implements BusyWaitStrategy {
    public static final ParkBusyWaitStrategy DEFAULT_INSTANCE = new ParkBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        Unsafe.park(1L);
    }
}
