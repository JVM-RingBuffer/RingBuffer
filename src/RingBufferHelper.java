package eu.menzani.ringbuffer;

class RingBufferHelper {
    static <T> T shouldBeAdvancing() {
        throw new AssertionError("This should have been an advancing-supporting implementation.");
    }

    static void shouldNotBeAdvancing() {
        throw new AssertionError("This should not have been an advancing-supporting implementation.");
    }
}
