package eu.menzani.ringbuffer;

public class YieldBusyWaitStrategy implements BusyWaitStrategy {
    public static final YieldBusyWaitStrategy INSTANCE = new YieldBusyWaitStrategy();

    @Override
    public void tick() {
        Thread.yield();
    }
}
