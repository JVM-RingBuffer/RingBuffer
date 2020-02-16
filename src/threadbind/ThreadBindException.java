package eu.menzani.ringbuffer.threadbind;

public class ThreadBindException extends Exception {
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
