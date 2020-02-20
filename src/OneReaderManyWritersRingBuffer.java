package eu.menzani.ringbuffer;

class OneReaderManyWritersRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterRingBuffer delegate;

    OneReaderManyWritersRingBuffer(RingBufferBuilder options) {
        delegate = new OneReaderOneWriterRingBuffer<>(options);
    }

    @Override
    public int getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public T put() {
        return (T) delegate.put();
    }

    @Override
    public void commit() {
        delegate.commit();
    }

    @Override
    public synchronized void put(T element) {
        delegate.put(element);
    }

    @Override
    public T take() {
        return (T) delegate.take();
    }

    // Must be called from the reader thread
    @Override
    public boolean contains(T element) {
        return delegate.contains(element);
    }

    // Must be called from the reader thread
    @Override
    public int size() {
        return delegate.size();
    }

    // Must be called from the reader thread
    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    // Must be called from the reader thread
    @Override
    public String toString() {
        return delegate.toString();
    }
}
