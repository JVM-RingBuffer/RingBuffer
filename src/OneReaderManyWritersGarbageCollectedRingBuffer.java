package eu.menzani.ringbuffer;

public class OneReaderManyWritersGarbageCollectedRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterGarbageCollectedRingBuffer delegate;

    public OneReaderManyWritersGarbageCollectedRingBuffer(int capacity) {
        delegate = new OneReaderOneWriterGarbageCollectedRingBuffer(capacity);
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

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
