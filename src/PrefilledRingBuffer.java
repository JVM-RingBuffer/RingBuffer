package eu.menzani.ringbuffer;

public interface PrefilledRingBuffer<T> extends AbstractRingBuffer<T> {
    T put();

    void commit();
}
