package org.ringbuffer.wait;

public class BusyWaitException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final boolean wasReading;

    public BusyWaitException(String message, boolean wasReading) {
        this(message, null, true, false, wasReading);
    }

    public BusyWaitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, boolean wasReading) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.wasReading = wasReading;
    }

    public boolean wasReading() {
        return wasReading;
    }

    public boolean wasWriting() {
        return !wasReading;
    }
}
