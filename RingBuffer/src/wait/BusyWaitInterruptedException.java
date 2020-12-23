package org.ringbuffer.wait;

public class BusyWaitInterruptedException extends BusyWaitException {
    public static final BusyWaitInterruptedException WHILE_READING = new BusyWaitInterruptedException(true);
    public static final BusyWaitInterruptedException WHILE_WRITING = new BusyWaitInterruptedException(false);

    public static BusyWaitInterruptedException getInstance(boolean whileReading) {
        return whileReading ? WHILE_READING : WHILE_WRITING;
    }

    private static final long serialVersionUID = 0L;

    protected BusyWaitInterruptedException(boolean wasReading) {
        super(null, wasReading);
    }
}
