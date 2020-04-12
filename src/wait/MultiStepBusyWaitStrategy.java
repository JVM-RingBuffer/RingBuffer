package eu.menzani.ringbuffer.wait;

import java.util.List;

public interface MultiStepBusyWaitStrategy extends BusyWaitStrategy {
    List<BusyWaitStrategy> getStrategies();

    List<Integer> getStrategiesTicks();

    interface Builder {
        Builder endWith(BusyWaitStrategy finalStrategy);

        Builder after(BusyWaitStrategy strategy, int strategyTicks);

        MultiStepBusyWaitStrategy build();
    }
}
