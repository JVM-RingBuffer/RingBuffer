package eu.menzani.ringbuffer;

class RingBufferHelper {
    static <T> T shouldNotBeGarbageCollected() {
        throw new AssertionError("This should not have been a garbage collected implementation.");
    }

    static <T> T readingIsNotAtomic() {
        throw new UnsupportedOperationException("Reading is not atomic.");
    }
}
