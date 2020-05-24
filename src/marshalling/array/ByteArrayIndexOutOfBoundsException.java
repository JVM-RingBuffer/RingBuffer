package eu.menzani.ringbuffer.marshalling.array;

public class ByteArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private final int index;

    ByteArrayIndexOutOfBoundsException(int index) {
        super(Integer.toString(index));
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
