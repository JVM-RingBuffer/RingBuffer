package eu.menzani.ringbuffer.system;

import java.util.OptionalInt;

public class ThreadBindException extends RuntimeException {
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
        this.errorCode = errorCode;
    }

    public OptionalInt getErrorCode() {
        if (errorCode == 0) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(errorCode);
    }
}
