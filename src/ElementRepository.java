package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public final class ElementRepository<T> {
    private final Object[] elements;
    private final int capacity;

    private int position;

    public ElementRepository(int capacity, Supplier<T> filler) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        elements = new Object[capacity];
        this.capacity = capacity;

        for (int i = 0; i < capacity; i++) {
            elements[i] = filler.get();
        }
        position = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void insert(Object element) {
        elements[position++] = element;
    }

    public T take() {
        return (T) elements[--position];
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
