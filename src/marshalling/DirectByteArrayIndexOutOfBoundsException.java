package org.ringbuffer.marshalling;

public class DirectByteArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private final long index;

    public DirectByteArrayIndexOutOfBoundsException(long index) {
        super(Long.toString(index));
        this.index = index;
    }

    public long getIndex() {
        return index;
    }
}
