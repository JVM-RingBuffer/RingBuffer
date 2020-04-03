package eu.menzani.ringbuffer.wait;

public class YieldBusyWaitStrategy implements BusyWaitStrategy {
    public static final YieldBusyWaitStrategy DEFAULT_INSTANCE = new YieldBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return BusyWaitStrategy.YIELD.newInstanceOrReusedIfThreadSafe();
    }

    @Override
    public void tick() {
        Thread.yield();
    }

    static class Factory implements BusyWaitStrategy.Factory {
        @Override
        public BusyWaitStrategy newInstanceOrReusedIfThreadSafe() {
            return MultiStepBusyWaitStrategy.endWith(DEFAULT_INSTANCE)
                    .after(HintBusyWaitStrategy.getDefault(), 100)
                    .build();
        }
    }
}
