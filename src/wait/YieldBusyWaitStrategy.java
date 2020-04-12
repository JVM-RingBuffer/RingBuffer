package eu.menzani.ringbuffer.wait;

public class YieldBusyWaitStrategy implements BusyWaitStrategy {
    public static final YieldBusyWaitStrategy DEFAULT_INSTANCE = new YieldBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return LinkedMultiStepBusyWaitStrategy.endWith(DEFAULT_INSTANCE)
                .after(HintBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    @Override
    public void tick() {
        Thread.yield();
    }
}
