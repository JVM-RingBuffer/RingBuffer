package eu.menzani.ringbuffer.marshalling;

public class ByteArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private final int index;

    public ByteArrayIndexOutOfBoundsException(int index) {
        super(Integer.toString(index));
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
