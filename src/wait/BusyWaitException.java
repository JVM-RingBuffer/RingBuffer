package eu.menzani.ringbuffer.wait;

public class BusyWaitException extends RuntimeException {
    private final boolean wasReading;

    public static BusyWaitException whileReading(String message) {
        return new BusyWaitException(message, null, true);
    }

    public static BusyWaitException whileWriting(String message) {
        return new BusyWaitException(message, null, false);
    }

    protected BusyWaitException(String message, Throwable cause, boolean wasReading) {
        super(message, cause);
        this.wasReading = wasReading;
    }

    public boolean wasReading() {
        return wasReading;
    }

    public boolean wasWriting() {
        return !wasReading;
    }
}
