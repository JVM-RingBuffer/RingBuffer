package eu.menzani.ringbuffer.wait;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    private static final HintBusyWaitStrategy instance = new HintBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return instance;
    }

    @Override
    public void tick() {
        Thread.onSpinWait();
    }
}
