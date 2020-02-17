package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public class OneReaderManyWritersBlockingRingBuffer<T> implements RingBuffer<T>, PrefilledRingBuffer<T> {
    private final OneReaderOneWriterBlockingRingBuffer delegate;

    public OneReaderManyWritersBlockingRingBuffer(int capacity) {
        delegate = new OneReaderOneWriterBlockingRingBuffer(capacity);
    }

    public OneReaderManyWritersBlockingRingBuffer(int capacity, Supplier<? extends T> filler) {
        delegate = new OneReaderOneWriterBlockingRingBuffer(capacity, filler);
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
