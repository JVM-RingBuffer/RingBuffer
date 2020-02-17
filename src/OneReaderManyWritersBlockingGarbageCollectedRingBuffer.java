package eu.menzani.ringbuffer;

public class OneReaderManyWritersBlockingGarbageCollectedRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterBlockingGarbageCollectedRingBuffer delegate;

    public OneReaderManyWritersBlockingGarbageCollectedRingBuffer(int capacity) {
        delegate = new OneReaderOneWriterBlockingGarbageCollectedRingBuffer(capacity);
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
