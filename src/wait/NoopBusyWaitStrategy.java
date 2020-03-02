package eu.menzani.ringbuffer.wait;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    public static BusyWaitStrategy getDefault() {
        return new NoopBusyWaitStrategy();
    }

    @Override
    public void tick() {}
}
