package org.ringbuffer.wait;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    public static final HintBusyWaitStrategy DEFAULT_INSTANCE = new HintBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public void reset() {}

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
