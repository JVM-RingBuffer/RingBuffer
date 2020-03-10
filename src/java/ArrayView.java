package eu.menzani.ringbuffer.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Collection;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class ArrayView<T> implements AbstractArray<T> {
    private static final String readOnlyMessage = "This instance does not permit write access.";
    private static final VarHandle ITERATOR = new VarHandleLookup(MethodHandles.lookup(), ArrayView.class).getVarHandle(IteratorView.class, "iterator");

    private final Array<T> delegate;
    private IteratorView<T> iterator;

    ArrayView(Array<T> delegate) {
        this.delegate = delegate;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        return delegate.containsAll(elements);
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(readOnlyMessage);
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
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void setPlain(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void setOpaque(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void setRelease(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void setVolatile(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void removePlain(int index) {
        throw new UnsupportedOperationException(readOnlyMessage);
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
        return getIterator();
    }

    private IteratorView<T> getIterator() {
        return LazyInit.get(ITERATOR, this, () -> new IteratorView<>(delegate.getIterator()));
    }

    @Override
    public AbstractArray<T> subList(int fromIndex, int toIndex) {
        return new SubArrayView(delegate.subList(fromIndex, toIndex));
    }

    @Override
    public boolean compareAndSet(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T compareAndExchange(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T getAndSet(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T getAndSetAcquire(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public T getAndSetRelease(int index, T element) {
        throw new UnsupportedOperationException(readOnlyMessage);
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
    public AbstractArray<T> unmodifiableView() {
        return this;
    }

    @Override
    public AbstractArray<T> immutableSnapshot() {
        return delegate.immutableSnapshot();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException(readOnlyMessage);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException(readOnlyMessage);
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
        throw new UnsupportedOperationException(readOnlyMessage);
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

    private static class IteratorView<T> implements ArrayIterator<T> {
        private final ArrayIterator<T> delegate;

        IteratorView(ArrayIterator<T> delegate) {
            this.delegate = delegate;
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
        public int nextAbsoluteIndex() {
            return delegate.nextAbsoluteIndex();
        }

        @Override
        public int previousAbsoluteIndex() {
            return delegate.previousAbsoluteIndex();
        }

        @Override
        public void setOpaque(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void setRelease(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void setVolatile(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSet(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSetAcquire(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSetRelease(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
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
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void set(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void add(T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
        }
    }

    class SubArrayView implements AbstractArray<T> {
        private final AbstractArray<T> delegate;

        SubArrayView(AbstractArray<T> delegate) {
            this.delegate = delegate;
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
            return getIterator();
        }

        @Override
        public AbstractArray<T> subList(int fromIndex, int toIndex) {
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
        public void setPlain(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void setOpaque(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void setRelease(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void setVolatile(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void removePlain(int index) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean compareAndSet(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchange(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSet(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSetAcquire(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T getAndSetRelease(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public AbstractArray<T> unmodifiableView() {
            return this;
        }

        @Override
        public AbstractArray<T> immutableSnapshot() {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean containsAll(Collection<?> elements) {
            return delegate.containsAll(elements);
        }

        @Override
        public boolean addAll(Collection<? extends T> elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> elements) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean removeAll(Collection<?> elements) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public boolean retainAll(Collection<?> elements) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void replaceAll(UnaryOperator<T> operator) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void sort(Comparator<? super T> comparator) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException(readOnlyMessage);
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
        public T get(int index) {
            return delegate.get(index);
        }

        @Override
        public T set(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public void add(int index, T element) {
            throw new UnsupportedOperationException(readOnlyMessage);
        }

        @Override
        public T remove(int index) {
            throw new UnsupportedOperationException(readOnlyMessage);
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
            throw new UnsupportedOperationException(readOnlyMessage);
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
}
