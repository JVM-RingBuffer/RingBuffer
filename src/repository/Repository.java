package eu.menzani.ringbuffer.repository;

import java.util.function.Supplier;

public final class Repository<T> {
    private final Object[] buffer;
    private final int capacity;

    private int position;

    public Repository(int capacity, Supplier<T> filler) {
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

    public void insert(Object element) {
        buffer[position++] = element;
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
