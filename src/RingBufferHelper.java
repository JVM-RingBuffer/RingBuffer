package eu.menzani.ringbuffer;

class RingBufferHelper {
    static <T> T shouldNotBeGarbageCollected() {
        throw new AssertionError("This should not have been a garbage collected implementation.");
    }
}
