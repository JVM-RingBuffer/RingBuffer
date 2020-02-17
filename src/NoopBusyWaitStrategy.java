package eu.menzani.ringbuffer;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    public static final NoopBusyWaitStrategy INSTANCE = new NoopBusyWaitStrategy();

    @Override
    public void tick() {
    }
}
