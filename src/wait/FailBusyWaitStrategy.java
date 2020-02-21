package eu.menzani.ringbuffer.wait;

import java.util.function.Supplier;

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    private static final FailBusyWaitStrategy readingTooSlow = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileWriting("The reading side is slower than expected."));
    private static final FailBusyWaitStrategy writingTooSlow = new FailBusyWaitStrategy(
            () -> BusyWaitException.whileReading("The writing side is slower than expected."));

    public static BusyWaitStrategy readingTooSlow() {
        return readingTooSlow;
    }

    public static BusyWaitStrategy writingTooSlow() {
        return writingTooSlow;
    }

    private final Supplier<? extends BusyWaitException> exceptionSupplier;

    public FailBusyWaitStrategy(Supplier<? extends BusyWaitException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public void tick() {
        BusyWaitException e = exceptionSupplier.get();
        e.fillInStackTrace();
        throw e;
    }
}
