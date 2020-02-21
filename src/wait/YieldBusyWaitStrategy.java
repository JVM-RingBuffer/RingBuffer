package eu.menzani.ringbuffer.wait;

public class YieldBusyWaitStrategy implements BusyWaitStrategy {
    private static final YieldBusyWaitStrategy instance = new YieldBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return MultiStepBusyWaitStrategy.endWith(instance)
                .after(HintBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    @Override
    public void tick() {
        Thread.yield();
    }
}
