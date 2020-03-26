package eu.menzani.ringbuffer.java;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

/**
 * Concurrent iteration is not supported nor detected.
 */
public interface Array<T> extends List<T>, RandomAccess, Comparable<Array<T>>, Cloneable {
    static Array<?> empty() {
        return MutableArray.EMPTY;
    }

    /**
     * This method is equivalent to {@link Array#allocate(int)}.
     * Use this method to declare intent. Use the other one when creating an array that will be populated.
     */
    static <T> Array<T> empty(int capacity) {
        return new MutableArray<>(capacity);
    }

    static <T> Array<T> allocate(int capacity) {
        return new MutableArray<>(capacity);
    }

    static <T> Array<T> fromCollection(Collection<T> collection) {
        return new MutableArray<>(collection);
    }

    static <T> Array<T> copyOf(T[] array) {
        return MutableArray.fromArray(array);
    }

    @SafeVarargs
    static <T> Array<T> of(T... elements) {
        return new MutableArray<>(elements);
    }

    ArrayIterator<T> iterator();

    ArrayIterator<T> listIterator();

    ArrayIterator<T> listIterator(int index);

    SubArray<T> subList(int fromIndex, int toIndex);

    T[] getBackingArray();

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

    int mismatch(Array<T> array);

    int mismatch(Array<T> array, Comparator<? super T> comparator);

    Array<T> unmodifiableView();

    Array<T> immutableSnapshot();

    Array<T> clone();
}
