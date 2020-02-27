package eu.menzani.ringbuffer.system;

public class ThreadBindException extends RuntimeException {
    private final int errorCode;

    ThreadBindException(Throwable cause) {
        super(cause);
        errorCode = 0;
    }

    ThreadBindException(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != 0;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
