package org.ringbuffer.wait;

import eu.menzani.atomic.AtomicBoolean;
import eu.menzani.lang.Lang;

public class InterruptibleBusyWaitStrategy implements BusyWaitStrategy {
    private static final long INTERRUPTED = Lang.objectFieldOffset(InterruptibleBusyWaitStrategy.class, "interrupted");

    private final BusyWaitInterruptedException exception;
    private final BusyWaitStrategy nextStrategy;

    private boolean interrupted;

    public InterruptibleBusyWaitStrategy(boolean whileReading, BusyWaitStrategy nextStrategy) {
        this(BusyWaitInterruptedException.getInstance(whileReading), nextStrategy);
    }

    public InterruptibleBusyWaitStrategy(BusyWaitInterruptedException exception, BusyWaitStrategy nextStrategy) {
        this.exception = exception;
        this.nextStrategy = nextStrategy;
    }

    public void interrupt() {
        AtomicBoolean.setOpaque(this, INTERRUPTED, true);
    }

    @Override
    public void reset() {
        nextStrategy.reset();
    }

    @Override
    public void tick() {
        if (AtomicBoolean.getOpaque(this, INTERRUPTED)) {
            AtomicBoolean.setOpaque(this, INTERRUPTED, false);
            throw exception;
        }
        nextStrategy.tick();
    }
}
