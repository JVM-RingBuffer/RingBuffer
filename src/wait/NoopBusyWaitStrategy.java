package eu.menzani.ringbuffer.wait;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    @Override
    public void reset() {
    }

    @Override
    public void tick() {
    }
}
