package eu.menzani.ringbuffer.wait;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
