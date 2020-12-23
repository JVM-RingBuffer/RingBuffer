package org.ringbuffer.wait;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    public static final NoopBusyWaitStrategy DEFAULT_INSTANCE = new NoopBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
    }
}
