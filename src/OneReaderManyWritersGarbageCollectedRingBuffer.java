package eu.menzani.ringbuffer;

public class OneReaderManyWritersGarbageCollectedRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterGarbageCollectedRingBuffer delegate;

    public OneReaderManyWritersGarbageCollectedRingBuffer(RingBufferOptions<?> options) {
        delegate = new OneReaderOneWriterGarbageCollectedRingBuffer<>(options);
    }

    @Override
    public int getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public synchronized void put(T element) {
        delegate.put(element);
    }

    @Override
    public T take() {
        return (T) delegate.take();
    }

    /** Must be called from the reader thread. */
    @Override
    public int size() {
        return delegate.size();
    }

    /** Must be called from the reader thread. */
    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
