package eu.menzani.ringbuffer;

public interface AbstractRingBuffer {
    int getCapacity();

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean isEmpty();
}
