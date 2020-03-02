package eu.menzani.ringbuffer.wait;

import java.util.function.Supplier;

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    public static BusyWaitStrategy readingTooSlow() {
        return new FailBusyWaitStrategy(() -> BusyWaitException.whileWriting("The reading side is slower than expected."));
    }

    public static BusyWaitStrategy writingTooSlow() {
        return new FailBusyWaitStrategy(() -> BusyWaitException.whileReading("The writing side is slower than expected."));
    }

    private final Supplier<? extends BusyWaitException> exceptionSupplier;

    public FailBusyWaitStrategy(Supplier<? extends BusyWaitException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    @Override
    public void tick() {
        throw exceptionSupplier.get();
    }
}
