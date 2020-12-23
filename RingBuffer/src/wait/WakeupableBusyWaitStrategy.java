package org.ringbuffer.wait;

import eu.menzani.atomic.AtomicBoolean;
import eu.menzani.lang.Lang;

public class WakeupableBusyWaitStrategy implements BusyWaitStrategy {
    private static final long SHOULD_WAKEUP = Lang.objectFieldOffset(WakeupableBusyWaitStrategy.class, "shouldWakeup");

    private final int maxIterations;
    private final BusyWaitStrategy busyWaitStrategy;

    private boolean shouldWakeup;

    public WakeupableBusyWaitStrategy(int maxIterations) {
        this(maxIterations, HintBusyWaitStrategy.getDefault());
    }

    public WakeupableBusyWaitStrategy(int maxIterations, BusyWaitStrategy busyWaitStrategy) {
        this.maxIterations = maxIterations;
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
        for (int i = maxIterations; i != 0 && AtomicBoolean.getOpaque(this, SHOULD_WAKEUP); i--) {
            busyWaitStrategy.tick();
        }
        resetFlag();
    }

    private void resetFlag() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, true);
    }
}
