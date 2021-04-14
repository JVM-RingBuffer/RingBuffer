package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.object.ObjectFactory;
import eu.menzani.struct.Arrays;
import jdk.internal.vm.annotation.Contended;

public class Stack<T> {
    @Contended
    private final T[] elements;
    @Contended
    private int index;

    public Stack(int capacity) {
        elements = Arrays.allocateGeneric(capacity);
    }

    public int getCapacity() {
        return elements.length;
    }

    public void pushMany(int amount, ObjectFactory<T> factory) {
        int toIndex = index + amount;
        for (int i = index; i < toIndex; i++) {
            AtomicArray.setPlain(elements, i, factory.newInstance());
        }
        index = toIndex;
    }

    public boolean canPush() {
        return index != elements.length;
    }

    public boolean canPop() {
        return index != 0;
    }

    public void push(T element) {
        AtomicArray.setPlain(elements, index++, element);
    }

    public T pop() {
        return AtomicArray.getPlain(elements, --index);
    }

    public T peek() {
        return AtomicArray.getPlain(elements, index);
    }

    public synchronized boolean isFull() {
        return index == elements.length;
    }

    public synchronized boolean isEmpty() {
        return index == 0;
    }

    public synchronized boolean isNotEmpty() {
        return index != 0;
    }

    public synchronized void put(T element) {
        AtomicArray.setPlain(elements, index++, element);
    }

    public synchronized T take() {
        return AtomicArray.getPlain(elements, --index);
    }

    public synchronized T get() {
        return AtomicArray.getPlain(elements, index);
    }
}
