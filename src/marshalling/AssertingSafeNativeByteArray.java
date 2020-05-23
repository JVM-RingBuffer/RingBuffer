package eu.menzani.ringbuffer.marshalling;

public class AssertingSafeNativeByteArray extends SafeNativeByteArray {
    public AssertingSafeNativeByteArray(long length) {
        super(length);
    }

    @Override
    void checkBounds(long index) {
        assert index >= 0L && index < length;
    }
}
