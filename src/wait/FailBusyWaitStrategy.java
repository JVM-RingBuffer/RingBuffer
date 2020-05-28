package org.ringbuffer.wait;

import java.util.function.Supplier;

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    public static final FailBusyWaitStrategy READING_TOO_SLOW = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileWriting("The reading side is slower than expected."));
    public static final FailBusyWaitStrategy WRITING_TOO_SLOW = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileReading("The writing side is slower than expected."));

    public static BusyWaitStrategy readingTooSlow() {
        return LinkedMultiStepBusyWaitStrategy.endWith(READING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, 100)
                .build();
    }

    public static BusyWaitStrategy writingTooSlow() {
        return LinkedMultiStepBusyWaitStrategy.endWith(WRITING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, 100)
                .build();
    }

    private final Supplier<? extends BusyWaitException> exceptionSupplier;

    public FailBusyWaitStrategy(Supplier<? extends BusyWaitException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public void reset() {}

    @Override
    public void tick() {
        throw exceptionSupplier.get();
    }
}
