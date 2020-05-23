package eu.menzani.ringbuffer.marshalling.array;

public class NativeByteArrayIndexOutOfBoundsException extends RuntimeException {
    private final long index;

    NativeByteArrayIndexOutOfBoundsException(long index) {
        super(Long.toString(index));
        this.index = index;
    }

    public long getIndex() {
        return index;
    }
}
