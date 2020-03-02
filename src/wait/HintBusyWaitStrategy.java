package eu.menzani.ringbuffer.wait;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    public static BusyWaitStrategy getDefault() {
        return new HintBusyWaitStrategy();
    }

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
