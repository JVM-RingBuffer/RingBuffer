package eu.menzani.ringbuffer;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    public static final HintBusyWaitStrategy INSTANCE = new HintBusyWaitStrategy();

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
