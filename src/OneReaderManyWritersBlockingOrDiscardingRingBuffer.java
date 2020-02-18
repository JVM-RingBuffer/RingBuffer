package eu.menzani.ringbuffer;

class OneReaderManyWritersBlockingOrDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterBlockingOrDiscardingRingBuffer delegate;

    OneReaderManyWritersBlockingOrDiscardingRingBuffer(OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> delegate) {
        this.delegate = delegate;
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

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
