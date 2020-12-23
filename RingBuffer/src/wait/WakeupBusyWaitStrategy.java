package org.ringbuffer.wait;

import eu.menzani.atomic.AtomicBoolean;
import eu.menzani.lang.Lang;

public class WakeupBusyWaitStrategy implements BusyWaitStrategy {
    private static final long SHOULD_WAKEUP = Lang.objectFieldOffset(WakeupBusyWaitStrategy.class, "shouldWakeup");

    private final BusyWaitStrategy busyWaitStrategy;

    private boolean shouldWakeup;

    public WakeupBusyWaitStrategy() {
        this(HintBusyWaitStrategy.getDefault());
    }

    public WakeupBusyWaitStrategy(BusyWaitStrategy busyWaitStrategy) {
        this.busyWaitStrategy = busyWaitStrategy;
        resetFlag();
    }

    public void wakeup() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, false);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        var busyWaitStrategy = this.busyWaitStrategy;
        busyWaitStrategy.reset();
        while (AtomicBoolean.getOpaque(this, SHOULD_WAKEUP)) {
            busyWaitStrategy.tick();
        }
        resetFlag();
    }

    private void resetFlag() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, true);
    }
}
