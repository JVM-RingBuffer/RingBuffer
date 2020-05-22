package eu.menzani.ringbuffer;

public interface AbstractRingBuffer {
    int getCapacity();

    /**
     * If the ring buffer is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    boolean isEmpty();
}
