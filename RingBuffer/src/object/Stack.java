package org.ringbuffer.object;

import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

abstract class Stack<T> implements RingBuffer<T> {
    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void takeBatch(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takePlain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }
}
