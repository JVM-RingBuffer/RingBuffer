package eu.menzani.ringbuffer.marshalling.array;

public class DirectByteArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private final long index;

    DirectByteArrayIndexOutOfBoundsException(long index) {
        super(Long.toString(index));
        this.index = index;
    }

    public long getIndex() {
        return index;
    }
}
