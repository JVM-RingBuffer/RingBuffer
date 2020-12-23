package org.ringbuffer.wait;

public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final SleepBusyWaitStrategy DEFAULT_INSTANCE = new SleepBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        try {
            Thread.sleep(1L);
        } catch (InterruptedException ignored) {
        }
    }
}
