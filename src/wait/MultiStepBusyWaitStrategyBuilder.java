package eu.menzani.ringbuffer.wait;

public interface MultiStepBusyWaitStrategyBuilder {
    MultiStepBusyWaitStrategyBuilder endWith(BusyWaitStrategy finalStrategy);

    MultiStepBusyWaitStrategyBuilder after(BusyWaitStrategy strategy, int strategyTicks);

    BusyWaitStrategy build();
}
