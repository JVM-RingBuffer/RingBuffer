package org.ringbuffer.system;

import org.ringbuffer.java.Assume;
import org.ringbuffer.java.Ensure;

public class ThreadManipulationException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final int errorCode;

    ThreadManipulationException(Throwable cause) {
        super(cause);
        errorCode = 0;
    }

    ThreadManipulationException(String message) {
        super(message);
        errorCode = 0;
    }

    ThreadManipulationException(int errorCode) {
        super("Error code = " + errorCode);
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
