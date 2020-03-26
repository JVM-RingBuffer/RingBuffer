package eu.menzani.ringbuffer.java;

import java.util.ListIterator;

public interface ArrayIterator<T> extends ListIterator<T> {
    int back();

    int forward();

    T nextOpaque();

    T nextAcquire();

    T nextVolatile();

    T previousOpaque();

    T previousAcquire();

    T previousVolatile();

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

    void previousRemove();

    void previousSet(T element);

    void previousSetOpaque(T element);

    void previousSetRelease(T element);

    void previousSetVolatile(T element);

    boolean previousCompareAndSet(T expectedElement, T newElement);

    T previousCompareAndExchange(T expectedElement, T newElement);

    T previousCompareAndExchangeAcquire(T expectedElement, T newElement);

    T previousCompareAndExchangeRelease(T expectedElement, T newElement);

    boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement);

    boolean previousWeakCompareAndSet(T expectedElement, T newElement);

    boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement);

    boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement);

    T previousGetAndSet(T element);

    T previousGetAndSetAcquire(T element);

    T previousGetAndSetRelease(T element);
}
