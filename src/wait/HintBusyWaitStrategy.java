package eu.menzani.ringbuffer.wait;

public class HintBusyWaitStrategy implements BusyWaitStrategy {
    public static final HintBusyWaitStrategy DEFAULT_INSTANCE = new HintBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return BusyWaitStrategy.HINT.newInstanceOrReusedIfThreadSafe();
    }

    @Override
    public void tick() {
        Thread.onSpinWait();
    }

    static class Factory implements BusyWaitStrategy.Factory {
        @Override
        public BusyWaitStrategy newInstanceOrReusedIfThreadSafe() {
            return DEFAULT_INSTANCE;
        }
    }
}
