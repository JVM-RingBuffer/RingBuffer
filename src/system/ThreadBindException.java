package eu.menzani.ringbuffer.system;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Ensure;

public class ThreadBindException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final int errorCode;

    ThreadBindException(Throwable cause) {
        super(cause);
        errorCode = 0;
    }

    ThreadBindException(String message) {
        super(message);
        errorCode = 0;
    }

    ThreadBindException(int errorCode) {
        Assume.notZero(errorCode);
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != 0;
    }

    public int getErrorCode() {
        Ensure.notZero(errorCode);
        return errorCode;
    }
}
