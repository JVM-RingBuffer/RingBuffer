package eu.menzani.ringbuffer.repository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class Repository<T> {
    private final Object[] buffer;
    private final int capacity;

    private final AtomicInteger position;

    public Repository(int capacity, Supplier<T> filler) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;

        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
        position = new AtomicInteger(capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public void add(Object element) {
        buffer[position.getAndIncrement()] = element;
    }

    public T remove() {
        return (T) buffer[position.decrementAndGet()];
    }

    public int size() {
        return position.get();
    }

    public boolean isEmpty() {
        return position.get() == 0;
    }

    public boolean isNotEmpty() {
        return position.get() != 0;
    }
}
