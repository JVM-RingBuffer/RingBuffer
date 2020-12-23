package org.ringbuffer.wait;

import eu.menzani.system.Platform;

public class WaitBusyWaitStrategy {
    public static final BusyWaitStrategy DEFAULT_INSTANCE;

    static {
        if (Platform.current().isWindows()) {
            DEFAULT_INSTANCE = NtDelayBusyWaitStrategy.DEFAULT_INSTANCE;
        } else {
            DEFAULT_INSTANCE = ParkBusyWaitStrategy.DEFAULT_INSTANCE;
        }
    }

    public static BusyWaitStrategy getDefault() {
        if (Platform.current().isWindows()) {
            return NtDelayBusyWaitStrategy.getDefault();
        }
        return ParkBusyWaitStrategy.getDefault();
    }

    static BusyWaitStrategy createDefault(BusyWaitStrategy defaultInstance) {
        return ArrayMultiStepBusyWaitStrategy.endWith(defaultInstance)
                .after(YieldBusyWaitStrategy.getDefault(), 100)
                .build();
    }
}
