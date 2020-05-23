package eu.menzani.ringbuffer.marshalling;

public class ReportingSafeNativeByteArray extends SafeNativeByteArray {
    public ReportingSafeNativeByteArray(long length) {
        super(length);
    }

    @Override
    void checkBounds(long index) {
        if (index < 0L || index >= length) {
            throw new NativeByteArrayIndexOutOfBoundsException(index);
        }
    }
}
