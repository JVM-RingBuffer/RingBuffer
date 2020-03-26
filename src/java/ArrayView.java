package eu.menzani.ringbuffer.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Collection;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class ArrayView<T> implements Array<T> {
    private static final VarHandle ITERATOR;
    private static final VarHandle SUB_ARRAY_ITERATOR;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            ITERATOR = lookup.findVarHandle(ArrayView.class, "iterator", IteratorView.class);
            SUB_ARRAY_ITERATOR = lookup.findVarHandle(ArrayView.SubArrayView.class, "iterator", SubArrayIteratorView.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final MutableArray<T> delegate;
    private IteratorView<T> iterator;

    ArrayView(Array<T> delegate) {
        this((MutableArray<T>) delegate);
    }

    ArrayView(MutableArray<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T[] getBackingArray() {
        return delegate.getBackingArray();
    }

    @Override
    public int getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object element) {
        return delegate.contains(element);
    }

    @Override
    public ArrayIterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <U> U[] toArray(U[] array) {
        return delegate.toArray(array);
    }

    @Override
    public boolean add(T element) {
        return delegate.add(element);
    }

    @Override
    public boolean remove(Object element) {
        return unsupported();
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        return delegate.containsAll(elements);
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        return delegate.addAll(elements);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        return unsupported();
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        return unsupported();
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        return unsupported();
    }

    @Override
    public void clear() {
        unsupported();
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public T getOpaque(int index) {
        return delegate.getOpaque(index);
    }

    @Override
    public T getAcquire(int index) {
        return delegate.getAcquire(index);
    }

    @Override
    public T getVolatile(int index) {
        return delegate.getVolatile(index);
    }

    @Override
    public T set(int index, T element) {
        return unsupported();
    }

    @Override
    public void setElement(int index, T element) {
        unsupported();
    }

    @Override
    public void setOpaque(int index, T element) {
        unsupported();
    }

    @Override
    public void setRelease(int index, T element) {
        unsupported();
    }

    @Override
    public void setVolatile(int index, T element) {
        unsupported();
    }

    @Override
    public void add(int index, T element) {
        unsupported();
    }

    @Override
    public T remove(int index) {
        return unsupported();
    }

    @Override
    public void removeElement(int index) {
        unsupported();
    }

    @Override
    public int indexOf(Object element) {
        return delegate.indexOf(element);
    }

    @Override
    public int lastIndexOf(Object element) {
        return delegate.lastIndexOf(element);
    }

    @Override
    public ArrayIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ArrayIterator<T> listIterator(int index) {
        delegate.listIterator(index);
        return LazyInit.get(ITERATOR, this, view -> new IteratorView<>(view.delegate.getIterator()));
    }

    @Override
    public SubArray<T> subList(int fromIndex, int toIndex) {
        return new SubArrayView(delegate.subList(fromIndex, toIndex));
    }

    @Override
    public boolean compareAndSet(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public T compareAndExchange(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
        return unsupported();
    }

    @Override
    public T getAndSet(int index, T element) {
        return unsupported();
    }

    @Override
    public T getAndSetAcquire(int index, T element) {
        return unsupported();
    }

    @Override
    public T getAndSetRelease(int index, T element) {
        return unsupported();
    }

    @Override
    public void fill(T value) {
        unsupported();
    }

    @Override
    public void sort() {
        unsupported();
    }

    @Override
    public void parallelSort() {
        unsupported();
    }

    @Override
    public void parallelSort(Comparator<? super T> comparator) {
        unsupported();
    }

    @Override
    public int binarySearch(T element) {
        return delegate.binarySearch(element);
    }

    @Override
    public int binarySearch(T element, Comparator<? super T> comparator) {
        return delegate.binarySearch(element, comparator);
    }

    @Override
    public void parallelPrefix(BinaryOperator<T> operator) {
        unsupported();
    }

    @Override
    public void setAll(IntFunction<? extends T> generator) {
        unsupported();
    }

    @Override
    public void parallelSetAll(IntFunction<? extends T> generator) {
        unsupported();
    }

    @Override
    public int compareTo(Array<T> array) {
        return delegate.compareTo(array);
    }

    @Override
    public int mismatch(Array<T> array) {
        return delegate.mismatch(array);
    }

    @Override
    public int mismatch(Array<T> array, Comparator<? super T> comparator) {
        return delegate.mismatch(array, comparator);
    }

    @Override
    public Array<T> unmodifiableView() {
        return this;
    }

    @Override
    public Array<T> immutableSnapshot() {
        return delegate.immutableSnapshot();
    }

    @Override
    public Array<T> clone() {
        return delegate.clone();
    }

    @Override
    public boolean equals(Object object) {
        return delegate.equals(object);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        unsupported();
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        unsupported();
    }

    @Override
    public Spliterator<T> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return unsupported();
    }

    @Override
    public Stream<T> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        delegate.forEach(action);
    }

    private static <T> T unsupported() {
        throw new UnsupportedOperationException("This instance does not permit write access.");
    }

    private static class IteratorView<T> implements ArrayIterator<T> {
        private final MutableArray<T>.Iterator delegate;

        IteratorView(MutableArray<T>.Iterator delegate) {
            this.delegate = delegate;
        }

        @Override
        public int back() {
            return delegate.back();
        }

        @Override
        public int forward() {
            return delegate.forward();
        }

        @Override
        public T nextOpaque() {
            return delegate.nextOpaque();
        }

        @Override
        public T nextAcquire() {
            return delegate.nextAcquire();
        }

        @Override
        public T nextVolatile() {
            return delegate.nextVolatile();
        }

        @Override
        public T previousOpaque() {
            return delegate.previousOpaque();
        }

        @Override
        public T previousAcquire() {
            return delegate.previousAcquire();
        }

        @Override
        public T previousVolatile() {
            return delegate.previousVolatile();
        }

        @Override
        public void setOpaque(T element) {
            unsupported();
        }

        @Override
        public void setRelease(T element) {
            unsupported();
        }

        @Override
        public void setVolatile(T element) {
            unsupported();
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T getAndSet(T element) {
            return unsupported();
        }

        @Override
        public T getAndSetAcquire(T element) {
            return unsupported();
        }

        @Override
        public T getAndSetRelease(T element) {
            return unsupported();
        }

        @Override
        public void previousRemove() {
            unsupported();
        }

        @Override
        public void previousSet(T element) {
            unsupported();
        }

        @Override
        public void previousSetOpaque(T element) {
            unsupported();
        }

        @Override
        public void previousSetRelease(T element) {
            unsupported();
        }

        @Override
        public void previousSetVolatile(T element) {
            unsupported();
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousGetAndSet(T element) {
            return unsupported();
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return unsupported();
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return unsupported();
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public T next() {
            return delegate.next();
        }

        @Override
        public boolean hasPrevious() {
            return delegate.hasPrevious();
        }

        @Override
        public T previous() {
            return delegate.previous();
        }

        @Override
        public int nextIndex() {
            return delegate.nextIndex();
        }

        @Override
        public int previousIndex() {
            return delegate.previousIndex();
        }

        @Override
        public void remove() {
            unsupported();
        }

        @Override
        public void set(T element) {
            unsupported();
        }

        @Override
        public void add(T element) {
            unsupported();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
        }
    }

    class SubArrayView implements SubArray<T> {
        private final MutableArray<T>.MutableSubArray delegate;
        private SubArrayIteratorView<T> iterator;

        @VisibleForPerformance
        SubArrayView(SubArray<T> delegate) {
            this((MutableArray<T>.MutableSubArray) delegate);
        }

        SubArrayView(MutableArray<T>.MutableSubArray delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getBeginIndex() {
            return delegate.getBeginIndex();
        }

        @Override
        public int getEndIndex() {
            return delegate.getEndIndex();
        }

        @Override
        public T[] getBackingArray() {
            return delegate.getBackingArray();
        }

        @Override
        public ArrayIterator<T> iterator() {
            return listIterator();
        }

        @Override
        public ArrayIterator<T> listIterator() {
            return listIterator(0);
        }

        @Override
        public ArrayIterator<T> listIterator(int index) {
            delegate.listIterator(index);
            return LazyInit.get(SUB_ARRAY_ITERATOR, this, view -> new SubArrayIteratorView<>(view.delegate.getIterator()));
        }

        @Override
        public SubArray<T> subList(int fromIndex, int toIndex) {
            return new SubArrayView(delegate.subList(fromIndex, toIndex));
        }

        @Override
        public int getCapacity() {
            return delegate.getCapacity();
        }

        @Override
        public T getOpaque(int index) {
            return delegate.getOpaque(index);
        }

        @Override
        public T getAcquire(int index) {
            return delegate.getAcquire(index);
        }

        @Override
        public T getVolatile(int index) {
            return delegate.getVolatile(index);
        }

        @Override
        public void setElement(int index, T element) {
            unsupported();
        }

        @Override
        public void setOpaque(int index, T element) {
            unsupported();
        }

        @Override
        public void setRelease(int index, T element) {
            unsupported();
        }

        @Override
        public void setVolatile(int index, T element) {
            unsupported();
        }

        @Override
        public void removeElement(int index) {
            unsupported();
        }

        @Override
        public boolean compareAndSet(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchange(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T getAndSet(int index, T element) {
            return unsupported();
        }

        @Override
        public T getAndSetAcquire(int index, T element) {
            return unsupported();
        }

        @Override
        public T getAndSetRelease(int index, T element) {
            return unsupported();
        }

        @Override
        public void fill(T value) {
            unsupported();
        }

        @Override
        public void sort() {
            unsupported();
        }

        @Override
        public void parallelSort() {
            unsupported();
        }

        @Override
        public void parallelSort(Comparator<? super T> comparator) {
            unsupported();
        }

        @Override
        public int binarySearch(T element) {
            return delegate.binarySearch(element);
        }

        @Override
        public int binarySearch(T element, Comparator<? super T> comparator) {
            return delegate.binarySearch(element, comparator);
        }

        @Override
        public void parallelPrefix(BinaryOperator<T> operator) {
            unsupported();
        }

        @Override
        public void setAll(IntFunction<? extends T> generator) {
            unsupported();
        }

        @Override
        public void parallelSetAll(IntFunction<? extends T> generator) {
            unsupported();
        }

        @Override
        public int compareTo(Array<T> array) {
            return delegate.compareTo(array);
        }

        @Override
        public int mismatch(Array<T> array) {
            return delegate.mismatch(array);
        }

        @Override
        public int mismatch(Array<T> array, Comparator<? super T> comparator) {
            return delegate.mismatch(array, comparator);
        }

        @Override
        public SubArray<T> unmodifiableView() {
            return this;
        }

        @Override
        public Array<T> immutableSnapshot() {
            return delegate.immutableSnapshot();
        }

        @Override
        public Array<T> clone() {
            return delegate.clone();
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean contains(Object element) {
            return delegate.contains(element);
        }

        @Override
        public Object[] toArray() {
            return delegate.toArray();
        }

        @Override
        public <U> U[] toArray(U[] array) {
            return delegate.toArray(array);
        }

        @Override
        public boolean add(T element) {
            return delegate.add(element);
        }

        @Override
        public boolean remove(Object element) {
            return unsupported();
        }

        @Override
        public boolean containsAll(Collection<?> elements) {
            return delegate.containsAll(elements);
        }

        @Override
        public boolean addAll(Collection<? extends T> elements) {
            return delegate.addAll(elements);
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> elements) {
            return unsupported();
        }

        @Override
        public boolean removeAll(Collection<?> elements) {
            return unsupported();
        }

        @Override
        public boolean retainAll(Collection<?> elements) {
            return unsupported();
        }

        @Override
        public void replaceAll(UnaryOperator<T> operator) {
            unsupported();
        }

        @Override
        public void sort(Comparator<? super T> comparator) {
            unsupported();
        }

        @Override
        public void clear() {
            unsupported();
        }

        @Override
        public boolean equals(Object object) {
            return delegate.equals(object);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public T get(int index) {
            return delegate.get(index);
        }

        @Override
        public T set(int index, T element) {
            return unsupported();
        }

        @Override
        public void add(int index, T element) {
            unsupported();
        }

        @Override
        public T remove(int index) {
            return unsupported();
        }

        @Override
        public int indexOf(Object element) {
            return delegate.indexOf(element);
        }

        @Override
        public int lastIndexOf(Object element) {
            return delegate.lastIndexOf(element);
        }

        @Override
        public Spliterator<T> spliterator() {
            return delegate.spliterator();
        }

        @Override
        public <U> U[] toArray(IntFunction<U[]> generator) {
            return delegate.toArray(generator);
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            return unsupported();
        }

        @Override
        public Stream<T> stream() {
            return delegate.stream();
        }

        @Override
        public Stream<T> parallelStream() {
            return delegate.parallelStream();
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            delegate.forEach(action);
        }
    }

    private static class SubArrayIteratorView<T> implements SubArrayIterator<T> {
        private final MutableArray<T>.MutableSubArrayIterator delegate;

        SubArrayIteratorView(MutableArray<T>.MutableSubArrayIterator delegate) {
            this.delegate = delegate;
        }

        @Override
        public int back() {
            return delegate.back();
        }

        @Override
        public int forward() {
            return delegate.forward();
        }

        @Override
        public T nextOpaque() {
            return delegate.nextOpaque();
        }

        @Override
        public T nextAcquire() {
            return delegate.nextAcquire();
        }

        @Override
        public T nextVolatile() {
            return delegate.nextVolatile();
        }

        @Override
        public T previousOpaque() {
            return delegate.previousOpaque();
        }

        @Override
        public T previousAcquire() {
            return delegate.previousAcquire();
        }

        @Override
        public T previousVolatile() {
            return delegate.previousVolatile();
        }

        @Override
        public void setOpaque(T element) {
            unsupported();
        }

        @Override
        public void setRelease(T element) {
            unsupported();
        }

        @Override
        public void setVolatile(T element) {
            unsupported();
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T getAndSet(T element) {
            return unsupported();
        }

        @Override
        public T getAndSetAcquire(T element) {
            return unsupported();
        }

        @Override
        public T getAndSetRelease(T element) {
            return unsupported();
        }

        @Override
        public void previousRemove() {
            unsupported();
        }

        @Override
        public void previousSet(T element) {
            unsupported();
        }

        @Override
        public void previousSetOpaque(T element) {
            unsupported();
        }

        @Override
        public void previousSetRelease(T element) {
            unsupported();
        }

        @Override
        public void previousSetVolatile(T element) {
            unsupported();
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return unsupported();
        }

        @Override
        public T previousGetAndSet(T element) {
            return unsupported();
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return unsupported();
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return unsupported();
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public T next() {
            return delegate.next();
        }

        @Override
        public boolean hasPrevious() {
            return delegate.hasPrevious();
        }

        @Override
        public T previous() {
            return delegate.previous();
        }

        @Override
        public int nextIndex() {
            return delegate.nextIndex();
        }

        @Override
        public int previousIndex() {
            return delegate.previousIndex();
        }

        @Override
        public int nextAbsoluteIndex() {
            return delegate.nextAbsoluteIndex();
        }

        @Override
        public int previousAbsoluteIndex() {
            return delegate.previousAbsoluteIndex();
        }

        @Override
        public void remove() {
            unsupported();
        }

        @Override
        public void set(T element) {
            unsupported();
        }

        @Override
        public void add(T element) {
            unsupported();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
        }
    }
}
