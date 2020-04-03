package eu.menzani.ringbuffer.wait;

public class NoopBusyWaitStrategy implements BusyWaitStrategy {
    public static final NoopBusyWaitStrategy DEFAULT_INSTANCE = new NoopBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return BusyWaitStrategy.NOOP.newInstanceOrReusedIfThreadSafe();
    }

    @Override
    public void tick() {}

    static class Factory implements BusyWaitStrategy.Factory {
        @Override
        public BusyWaitStrategy newInstanceOrReusedIfThreadSafe() {
            return DEFAULT_INSTANCE;
        }
    }
}
