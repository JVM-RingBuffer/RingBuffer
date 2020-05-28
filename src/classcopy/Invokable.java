package org.ringbuffer.classcopy;

public interface Invokable<T> {
    T call(Object... arguments);
}
