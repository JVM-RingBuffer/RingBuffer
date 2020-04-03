package eu.menzani.ringbuffer.java;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

public interface Array<T> extends List<T>, RandomAccess, Comparable<Array<T>>, Cloneable {
    static Array<?> empty() {
        return MutableArray.EMPTY;
    }

    /**
     * This method is equivalent to {@link Array#allocate(int)}.
     * Use this method to declare intent. Use the other one when creating an array that will be populated.
     */
    @NeedsPublication
    static <T> Array<T> empty(int capacity) {
        return MutableArray.allocate(capacity);
    }

    @NeedsPublication
    static <T> Array<T> allocate(int capacity) {
        return MutableArray.allocate(capacity);
    }

    @NeedsPublication
    static <T> Array<T> fromCollection(Collection<T> collection) {
        return MutableArray.fromCollection(collection);
    }

    @NeedsPublication
    static <T> Array<T> copyOf(T[] array) {
        return MutableArray.copyOf(array);
    }

    @SafeVarargs
    @NeedsPublication
    static <T> Array<T> of(T... elements) {
        return MutableArray.of(elements);
    }

    @Override
    default boolean add(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addAll(Collection<? extends T> elements) {
        throw new UnsupportedOperationException();
    }

    /**
     * The iterator is a reused object: it can be used by at most one thread at a time.
     * Concurrent iteration is not detected.
     */
    default ArrayIterator<T> iterator() {
        return listIterator();
    }

    /**
     * The iterator is a reused object: it can be used by at most one thread at a time.
     * Concurrent iteration is not detected.
     */
    default ArrayIterator<T> listIterator() {
        return listIterator(0);
    }

    /**
     * The iterator is a reused object: it can be used by at most one thread at a time.
     * Concurrent iteration is not detected.
     */
    ArrayIterator<T> listIterator(int index);

    SubArray<T> subList(int fromIndex, int toIndex);

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

    /**
     * The iterator itself is not thread-safe: one instance must be constructed per thread.
     */
    default ArrayIterator<T> parallelIterator() {
        return parallelIterator(0);
    }

    /**
     * The iterator itself is not thread-safe: one instance must be constructed per thread.
     */
    ArrayIterator<T> parallelIterator(int index);

    default ArrayIterator<T> concurrentIterator() {
        return concurrentIterator(0);
    }

    ArrayIterator<T> concurrentIterator(int index);

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
