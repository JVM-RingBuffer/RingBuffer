package eu.menzani.ringbuffer.wait;

public class YieldBusyWaitStrategy implements BusyWaitStrategy {
    public static BusyWaitStrategy getDefault() {
        return MultiStepBusyWaitStrategy.endWith(new YieldBusyWaitStrategy())
                .after(HintBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    @Override
    public void tick() {
        Thread.yield();
    }
}
