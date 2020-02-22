package eu.menzani.ringbuffer;

class AtomicReadRingBuffer<T> implements RingBuffer<T> {
    private final VolatileRingBuffer delegate;

    AtomicReadRingBuffer(RingBufferBuilder<?> builder) {
        delegate = new VolatileRingBuffer<>(builder) {
            @Override
            synchronized int getReadPosition() {
                return super.getReadPosition();
            }
        };
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
    public T take() {
        synchronized (delegate) {
            return (T) delegate.take();
        }
    }

    @Override
    public int getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public boolean contains(T element) {
        return delegate.contains(element);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
