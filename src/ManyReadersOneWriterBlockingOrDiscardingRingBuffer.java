package eu.menzani.ringbuffer;

class ManyReadersOneWriterBlockingOrDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final OneReaderOneWriterBlockingOrDiscardingRingBuffer delegate;

    ManyReadersOneWriterBlockingOrDiscardingRingBuffer(OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> delegate) {
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
    public void put(T element) {
        delegate.put(element);
    }

    @Override
    public synchronized T take() {
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