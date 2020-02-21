package eu.menzani.ringbuffer.wait;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    private static final NoopBusyWaitStrategy instance = new NoopBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return instance;
    }

    @Override
    public void tick() {}
}
