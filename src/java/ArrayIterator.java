package eu.menzani.ringbuffer.java;

import java.util.ListIterator;

public interface ArrayIterator<T> extends ListIterator<T> {
    T nextOpaque();

    T nextAcquire();

    T nextVolatile();

    T previousOpaque();

    T previousAcquire();

    T previousVolatile();

    int nextAbsoluteIndex();

    int previousAbsoluteIndex();

    void setOpaque(T element);

    void setRelease(T element);

    void setVolatile(T element);

    boolean compareAndSet(T expectedElement, T newElement);

    T compareAndExchange(T expectedElement, T newElement);

    T compareAndExchangeAcquire(T expectedElement, T newElement);

    T compareAndExchangeRelease(T expectedElement, T newElement);

    boolean weakCompareAndSetPlain(T expectedElement, T newElement);

    boolean weakCompareAndSet(T expectedElement, T newElement);

    boolean weakCompareAndSetAcquire(T expectedElement, T newElement);

    boolean weakCompareAndSetRelease(T expectedElement, T newElement);

    T getAndSet(T element);

    T getAndSetAcquire(T element);

    T getAndSetRelease(T element);
}
