package eu.menzani.ringbuffer.java;

import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

public interface AbstractArray<T> extends List<T>, RandomAccess, Comparable<AbstractArray<T>>, Cloneable {
    ArrayIterator<T> iterator();

    ArrayIterator<T> listIterator();

    ArrayIterator<T> listIterator(int index);

    AbstractSubArray<T> subList(int fromIndex, int toIndex);

    T[] getElements();

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

    void fill(T value);

    void sort();

    void parallelSort();

    void parallelSort(Comparator<? super T> comparator);

    int binarySearch(T element);

    int binarySearch(T element, Comparator<? super T> comparator);

    void parallelPrefix(BinaryOperator<T> operator);

    void setAll(IntFunction<? extends T> generator);

    void parallelSetAll(IntFunction<? extends T> generator);

    int mismatch(AbstractArray<T> array);

    int mismatch(AbstractArray<T> array, Comparator<? super T> comparator);

    AbstractArray<T> unmodifiableView();

    AbstractArray<T> immutableSnapshot();

    Array<T> clone();
}
