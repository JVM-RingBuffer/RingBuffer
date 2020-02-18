package eu.menzani.ringbuffer;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
