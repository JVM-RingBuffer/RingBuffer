package eu.menzani.ringbuffer.marshalling;

public class ReportingAssertingSafeNativeByteArray extends SafeNativeByteArray {
    public ReportingAssertingSafeNativeByteArray(long length) {
        super(length);
    }

    @Override
    void checkBounds(long index) {
        assert index >= 0L && index < length : index;
    }
}
