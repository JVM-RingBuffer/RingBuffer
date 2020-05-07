package eu.menzani.ringbuffer;

class RingBufferHelper {
    static <T> T shouldNotBeGarbageCollected() {
        throw new AssertionError("This should not have been a garbage collected implementation.");
    }

    static void shouldNotBeBlockingAndPrefilled() {
        throw new AssertionError("This should not have been a blocking and pre-filled implementation.");
    }
}
