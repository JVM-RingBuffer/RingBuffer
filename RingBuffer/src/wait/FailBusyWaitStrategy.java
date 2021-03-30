package org.ringbuffer.wait;

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    public static final FailBusyWaitStrategy READING_TOO_SLOW = new FailBusyWaitStrategy(new BusyWaitException("The reading side is slower than expected.", false));
    public static final FailBusyWaitStrategy WRITING_TOO_SLOW = new FailBusyWaitStrategy(new BusyWaitException("The writing side is slower than expected.", true));

    public static final FailBusyWaitStrategy WRITING_SHOULD_NOT_BLOCK = new FailBusyWaitStrategy(new BusyWaitException("Writing should not block.", false));
    public static final FailBusyWaitStrategy READING_SHOULD_NOT_BLOCK = new FailBusyWaitStrategy(new BusyWaitException("Reading should not block.", true));

    public static BusyWaitStrategy readingTooSlow(int timeBudget) {
        return LinkedMultiStepBusyWaitStrategy.endWith(READING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, timeBudget)
                .build();
    }

    public static BusyWaitStrategy writingTooSlow(int timeBudget) {
        return LinkedMultiStepBusyWaitStrategy.endWith(WRITING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, timeBudget)
                .build();
    }

    private final BusyWaitException exception;

    private FailBusyWaitStrategy(BusyWaitException exception) {
        this.exception = exception;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        throw exception;
    }
}
