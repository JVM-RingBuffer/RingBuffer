package eu.menzani.ringbuffer;

class RingBufferHelper {
    static <T> T keyRequired() {
        throw new UnsupportedOperationException("Use nextKey(), next(key) and put(key) instead.");
    }

    static <T> T shouldBeAdvancing() {
        throw new AssertionError("This should have been an advancing-supporting implementation.");
    }

    static void shouldNotBeAdvancing() {
        throw new AssertionError("This should not have been an advancing-supporting implementation.");
    }
}
