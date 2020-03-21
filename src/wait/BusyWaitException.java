package eu.menzani.ringbuffer.wait;

public class BusyWaitException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final boolean wasReading;

    public static BusyWaitException whileReading(String message) {
        return new BusyWaitException(message, null, true, true, true);
    }

    public static BusyWaitException whileWriting(String message) {
        return new BusyWaitException(message, null, true, true, false);
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
