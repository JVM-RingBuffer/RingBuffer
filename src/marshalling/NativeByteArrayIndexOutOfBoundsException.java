package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.java.Ensure;

public class NativeByteArrayIndexOutOfBoundsException extends RuntimeException {
    private final Long index;

    NativeByteArrayIndexOutOfBoundsException() {
        index = null;
    }

    NativeByteArrayIndexOutOfBoundsException(long index) {
        super(Long.toString(index));
        this.index = index;
    }

    public boolean hasIndex() {
        return index != null;
    }

    public long getIndex() {
        Ensure.notNull(index);
        return index;
    }
}
