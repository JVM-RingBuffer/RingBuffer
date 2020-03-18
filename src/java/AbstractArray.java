package eu.menzani.ringbuffer.java;

import java.util.List;
import java.util.RandomAccess;

public interface AbstractArray<T> extends List<T>, RandomAccess, Cloneable {
    ArrayIterator<T> iterator();

    ArrayIterator<T> listIterator();

    ArrayIterator<T> listIterator(int index);

    AbstractArray<T> subList(int fromIndex, int toIndex);

    int getCapacity();

    T getOpaque(int index);

    T getAcquire(int index);

    T getVolatile(int index);

    void setElement(int index, T element);

    void setOpaque(int index, T element);

    void setRelease(int index, T element);

    void setVolatile(int index, T element);

    void removeElement(int index);

    boolean compareAndSet(int index, T expectedElement, T newElement);

    T compareAndExchange(int index, T expectedElement, T newElement);

    T compareAndExchangeAcquire(int index, T expectedElement, T newElement);

    T compareAndExchangeRelease(int index, T expectedElement, T newElement);

    boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement);

    boolean weakCompareAndSet(int index, T expectedElement, T newElement);

    boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement);

    boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement);

    T getAndSet(int index, T element);

    T getAndSetAcquire(int index, T element);

    T getAndSetRelease(int index, T element);

    AbstractArray<T> unmodifiableView();

    AbstractArray<T> immutableSnapshot();

    Array<T> clone();
}
