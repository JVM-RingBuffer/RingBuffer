package eu.menzani.ringbuffer.wait;

import java.util.function.Supplier;

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    public static final FailBusyWaitStrategy READING_TOO_SLOW = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileWriting("The reading side is slower than expected."));
    public static final FailBusyWaitStrategy WRITING_TOO_SLOW = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileReading("The writing side is slower than expected."));

    public static BusyWaitStrategy readingTooSlow() {
        return READING_TOO_SLOW;
    }

    public static BusyWaitStrategy writingTooSlow() {
        return WRITING_TOO_SLOW;
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
