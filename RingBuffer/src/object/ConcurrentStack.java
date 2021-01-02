package org.ringbuffer.object;

import jdk.internal.vm.annotation.Contended;

public class ConcurrentStack<T> extends Stack<T> {
    @Contended
    private final T[] elements;
    @Contended
    private int index;

    @SuppressWarnings("unchecked")
    public ConcurrentStack(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    @Override
    public int getCapacity() {
        return elements.length;
    }

    public boolean canPush() {
        return index != elements.length;
    }

    public boolean canPop() {
        return index != 0;
    }

    public void push(T element) {
        elements[index++] = element;
    }

    public T pop() {
        return elements[--index];
    }

    public T peek() {
        return elements[index];
    }

    public synchronized boolean isFull() {
        return index == elements.length;
    }

    @Override
    public synchronized boolean isEmpty() {
        return index == 0;
    }

    @Override
    public synchronized boolean isNotEmpty() {
        return index != 0;
    }

    @Override
    public synchronized void put(T element) {
        elements[index++] = element;
    }

    @Override
    public synchronized T take() {
        return elements[--index];
    }

    public synchronized T get() {
        return elements[index];
    }
}
