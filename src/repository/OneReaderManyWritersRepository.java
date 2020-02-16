package eu.menzani.ringbuffer.repository;

import java.util.function.Supplier;

public final class OneReaderManyWritersRepository<T> {
    private final Object[] buffer;
    private final int capacity;

    private volatile int position;

    public OneReaderManyWritersRepository(int capacity, Supplier<T> filler) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;

        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
        position = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized void insert(Object element) {
        int position = this.position;
        buffer[position] = element;
        this.position = ++position;
    }

    public T take() {
        return (T) buffer[--position];
    }

    public int size() {
        return position;
    }

    public boolean isEmpty() {
        return position == 0;
    }

    public boolean isNotEmpty() {
        return position != 0;
    }
}
