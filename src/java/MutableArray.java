package eu.menzani.ringbuffer.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class MutableArray<T> implements Array<T>, Serializable {
    static final MutableArray<?> EMPTY = allocate(0);

    private static final long serialVersionUID = 0L;
    private static final Class<? extends Object[]> elementsArrayClass = Object[].class;
    private static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(elementsArrayClass);

    @VisibleForPerformance
    final T[] elements;

    private transient Iterator iterator;
    private transient ArrayView<T> view;

    static <T> MutableArray<T> allocate(int capacity) {
        MutableArray<T> result = new MutableArray<>(capacity);
        result.initializeComponents();
        return result;
    }

    static <T> MutableArray<T> fromCollection(Collection<T> collection) {
        MutableArray<T> result = new MutableArray<>(collection);
        result.initializeComponents();
        return result;
    }

    static <T> MutableArray<T> of(T[] elements) {
        MutableArray<T> result = new MutableArray<>(elements);
        result.initializeComponents();
        return result;
    }

    static <T> MutableArray<T> copyOf(T[] array) {
        MutableArray<T> result = new MutableArray<>((T[]) Arrays.copyOf(array, array.length, elementsArrayClass));
        result.initializeComponents();
        return result;
    }

    private MutableArray(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    private MutableArray(Collection<T> collection) {
        elements = (T[]) collection.toArray();
    }

    private MutableArray(T[] elements) {
        this.elements = elements;
    }

    private void initializeComponents() {
        iterator = new Iterator();
        view = new ArrayView<>(this);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        initializeComponents();
    }

    private void readObjectNoData() throws ObjectStreamException {
        initializeComponents();
    }

    Iterator getIterator() {
        return iterator;
    }

    @Override
    public T[] getElements() {
        return elements;
    }

    @Override
    public int getCapacity() {
        return elements.length;
    }

    @Override
    public int size() {
        int result = 0;
        for (Object element : elements) {
            if (element != null) {
                result++;
            }
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        for (Object element : elements) {
            if (element != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object element) {
        return indexOf(element) != -1;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, getCapacity(), elementsArrayClass);
    }

    @Override
    public <U> U[] toArray(U[] array) {
        int capacity = getCapacity();
        if (array.length < capacity) {
            return Arrays.copyOf(elements, capacity, (Class<? extends U[]>) array.getClass());
        }
        System.arraycopy(elements, 0, array, 0, capacity);
        return array;
    }

    @Override
    public boolean remove(Object element) {
        int i = indexOf(element);
        if (i == -1) {
            return false;
        }
        elements[i] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        for (Object element : elements) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        for (T element : elements) {
            this.elements[index++] = element;
        }
        return !elements.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        boolean changed = false;
        for (Object element : elements) {
            if (remove(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        boolean changed = false;
        for (int i = 0; i < getCapacity(); i++) {
            if (!elements.contains(this.elements[i])) {
                this.elements[i] = null;
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);
    }

    @Override
    public T get(int index) {
        return elements[index];
    }

    @Override
    public T getOpaque(int index) {
        return (T) ELEMENTS.getOpaque(elements, index);
    }

    @Override
    public T getAcquire(int index) {
        return (T) ELEMENTS.getAcquire(elements, index);
    }

    @Override
    public T getVolatile(int index) {
        return (T) ELEMENTS.getVolatile(elements, index);
    }

    @Override
    public T set(int index, T element) {
        T oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public void setElement(int index, T element) {
        elements[index] = element;
    }

    @Override
    public void setOpaque(int index, T element) {
        ELEMENTS.setOpaque(elements, index, element);
    }

    @Override
    public void setRelease(int index, T element) {
        ELEMENTS.setRelease(elements, index, element);
    }

    @Override
    public void setVolatile(int index, T element) {
        ELEMENTS.setVolatile(elements, index, element);
    }

    @Override
    public void add(int index, T element) {
        setElement(index, element);
    }

    @Override
    public T remove(int index) {
        return set(index, null);
    }

    @Override
    public void removeElement(int index) {
        setElement(index, null);
    }

    @Override
    public int indexOf(Object element) {
        for (int i = 0; i < getCapacity(); i++) {
            if (Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object element) {
        for (int i = getCapacity() - 1; i >= 0; i--) {
            if (Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ArrayIterator<T> listIterator(int index) {
        iterator.initialize(getCapacity(), index);
        return iterator;
    }

    @Override
    public SubArray<T> subList(int fromIndex, int toIndex) {
        return new MutableSubArray(fromIndex, toIndex);
    }

    @Override
    public boolean compareAndSet(int index, T expectedElement, T newElement) {
        return ELEMENTS.compareAndSet(elements, index, expectedElement, newElement);
    }

    @Override
    public T compareAndExchange(int index, T expectedElement, T newElement) {
        return (T) ELEMENTS.compareAndExchange(elements, index, expectedElement, newElement);
    }

    @Override
    public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
        return (T) ELEMENTS.compareAndExchangeAcquire(elements, index, expectedElement, newElement);
    }

    @Override
    public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
        return (T) ELEMENTS.compareAndExchangeRelease(elements, index, expectedElement, newElement);
    }

    @Override
    public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
        return ELEMENTS.weakCompareAndSetPlain(elements, index, expectedElement, newElement);
    }

    @Override
    public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
        return ELEMENTS.weakCompareAndSet(elements, index, expectedElement, newElement);
    }

    @Override
    public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
        return ELEMENTS.weakCompareAndSetAcquire(elements, index, expectedElement, newElement);
    }

    @Override
    public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
        return ELEMENTS.weakCompareAndSetRelease(elements, index, expectedElement, newElement);
    }

    @Override
    public T getAndSet(int index, T element) {
        return (T) ELEMENTS.getAndSet(elements, index, element);
    }

    @Override
    public T getAndSetAcquire(int index, T element) {
        return (T) ELEMENTS.getAndSetAcquire(elements, index, element);
    }

    @Override
    public T getAndSetRelease(int index, T element) {
        return (T) ELEMENTS.getAndSetRelease(elements, index, element);
    }

    @Override
    public ArrayIterator<T> parallelIterator(int index) {
        return new Iterator(getCapacity(), index);
    }

    @Override
    public ArrayIterator<T> concurrentIterator(int index) {
        return new ConcurrentIterator(getCapacity(), index);
    }

    @Override
    public void fill(T value) {
        Arrays.fill(elements, value);
    }

    @Override
    public void sort() {
        Arrays.sort(elements);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        Arrays.sort(elements, comparator);
    }

    @Override
    public void parallelSort() {
        Arrays.parallelSort((Comparable[]) elements);
    }

    @Override
    public void parallelSort(Comparator<? super T> comparator) {
        Arrays.parallelSort(elements, comparator);
    }

    @Override
    public Stream<T> stream() {
        return Arrays.stream(elements);
    }

    @Override
    public Stream<T> parallelStream() {
        return stream().parallel();
    }

    @Override
    public int binarySearch(T element) {
        return Arrays.binarySearch(elements, element);
    }

    @Override
    public int binarySearch(T element, Comparator<? super T> comparator) {
        return Arrays.binarySearch(elements, element, comparator);
    }

    @Override
    public void parallelPrefix(BinaryOperator<T> operator) {
        Arrays.parallelPrefix(elements, operator);
    }

    @Override
    public void setAll(IntFunction<? extends T> generator) {
        Arrays.setAll(elements, generator);
    }

    @Override
    public void parallelSetAll(IntFunction<? extends T> generator) {
        Arrays.parallelSetAll(elements, generator);
    }

    @Override
    public int compareTo(Array<T> array) {
        if (array instanceof SubArray<?>) {
            SubArray<T> subArray = (SubArray<T>) array;
            return Arrays.compare((Comparable[]) elements, 0, getCapacity(), (Comparable[]) array.getElements(), subArray.getBeginIndex(), subArray.getEndIndex());
        }
        return Arrays.compare((Comparable[]) elements, (Comparable[]) array.getElements());
    }

    public int mismatch(Array<T> array) {
        if (array instanceof SubArray<?>) {
            SubArray<T> subArray = (SubArray<T>) array;
            return Arrays.mismatch(elements, 0, getCapacity(), array.getElements(), subArray.getBeginIndex(), subArray.getEndIndex());
        }
        return Arrays.mismatch(elements, array.getElements());
    }

    public int mismatch(Array<T> array, Comparator<? super T> comparator) {
        int bFromIndex;
        int bToIndex;
        if (array instanceof SubArray<?>) {
            SubArray<T> subArray = (SubArray<T>) array;
            bFromIndex = subArray.getBeginIndex();
            bToIndex = subArray.getEndIndex();
        } else {
            bFromIndex = 0;
            bToIndex = array.getCapacity();
        }
        return Arrays.mismatch(elements, 0, getCapacity(), array.getElements(), bFromIndex, bToIndex, comparator);
    }

    @Override
    public Array<T> unmodifiableView() {
        return view;
    }

    @Override
    public Array<T> immutableSnapshot() {
        return new ArrayView<>(clone());
    }

    @Override
    public Array<T> clone() {
        return fromCollection(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Array)) {
            return false;
        }
        Array<?> that = (Array<?>) object;
        if (getCapacity() != that.getCapacity()) {
            return false;
        }
        ArrayIterator<?> iterator = iterator();
        ArrayIterator<?> thatIterator = that.iterator();
        while (iterator.hasNext() && thatIterator.hasNext()) {
            if (!Objects.equals(iterator.next(), thatIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    class Iterator implements ArrayIterator<T> {
        private int endIndex;
        private int index;

        @VisibleForPerformance
        Iterator() {}

        @VisibleForPerformance
        Iterator(int endIndex, int index) {
            this.endIndex = endIndex;
            this.index = index;
        }

        @VisibleForPerformance
        void initialize(int endIndex, int index) {
            this.endIndex = endIndex;
            this.index = index;
        }

        @Override
        public int back() {
            return --index;
        }

        @Override
        public int forward() {
            return index++;
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public T next() {
            return get(index++);
        }

        @Override
        public T nextOpaque() {
            return getOpaque(index++);
        }

        @Override
        public T nextAcquire() {
            return getAcquire(index++);
        }

        @Override
        public T nextVolatile() {
            return getVolatile(index++);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            return get(--index);
        }

        @Override
        public T previousOpaque() {
            return getOpaque(--index);
        }

        @Override
        public T previousAcquire() {
            return getAcquire(--index);
        }

        @Override
        public T previousVolatile() {
            return getVolatile(--index);
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            set(null);
        }

        @Override
        public void set(T element) {
            setElement(index - 1, element);
        }

        @Override
        public void setOpaque(T element) {
            MutableArray.this.setOpaque(index - 1, element);
        }

        @Override
        public void setRelease(T element) {
            MutableArray.this.setRelease(index - 1, element);
        }

        @Override
        public void setVolatile(T element) {
            MutableArray.this.setVolatile(index - 1, element);
        }

        @Override
        public void add(T element) {
            set(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index - 1, expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return MutableArray.this.getAndSet(index - 1, element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index - 1, element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index - 1, element);
        }

        @Override
        public void previousRemove() {
            previousSet(null);
        }

        @Override
        public void previousSet(T element) {
            setElement(index, element);
        }

        @Override
        public void previousSetOpaque(T element) {
            MutableArray.this.setOpaque(index, element);
        }

        @Override
        public void previousSetRelease(T element) {
            MutableArray.this.setRelease(index, element);
        }

        @Override
        public void previousSetVolatile(T element) {
            MutableArray.this.setVolatile(index, element);
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index, expectedElement, newElement);
        }

        @Override
        public T previousGetAndSet(T element) {
            return MutableArray.this.getAndSet(index, element);
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index, element);
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index, element);
        }
    }

    private class ConcurrentIterator implements ArrayIterator<T> {
        private final int endIndex;
        private final LazyVolatileInteger index;

        private final Lock lock = new ReentrantLock();

        ConcurrentIterator(int endIndex, int index) {
            this.endIndex = endIndex;
            this.index = new LazyVolatileInteger(index);
        }

        @Override
        public int back() {
            lock.lock();
            int newIndex = index.decrementAndGetPlain();
            lock.unlock();
            return newIndex;
        }

        @Override
        public int forward() {
            lock.lock();
            int newIndex = index.getPlainAndIncrement();
            lock.unlock();
            return newIndex;
        }

        @Override
        public boolean hasNext() {
            lock.lock();
            return index.getPlain() < endIndex;
        }

        @Override
        public T next() {
            int index = this.index.getPlainAndIncrement();
            lock.unlock();
            return get(index);
        }

        @Override
        public T nextOpaque() {
            int index = this.index.getPlainAndIncrement();
            lock.unlock();
            return getOpaque(index);
        }

        @Override
        public T nextAcquire() {
            int index = this.index.getPlainAndIncrement();
            lock.unlock();
            return getAcquire(index);
        }

        @Override
        public T nextVolatile() {
            int index = this.index.getPlainAndIncrement();
            lock.unlock();
            return getVolatile(index);
        }

        @Override
        public boolean hasPrevious() {
            lock.lock();
            return index.getPlain() > 0;
        }

        @Override
        public T previous() {
            int index = this.index.decrementAndGetPlain();
            lock.unlock();
            return get(index);
        }

        @Override
        public T previousOpaque() {
            int index = this.index.decrementAndGetPlain();
            lock.unlock();
            return getOpaque(index);
        }

        @Override
        public T previousAcquire() {
            int index = this.index.decrementAndGetPlain();
            lock.unlock();
            return getAcquire(index);
        }

        @Override
        public T previousVolatile() {
            int index = this.index.decrementAndGetPlain();
            lock.unlock();
            return getVolatile(index);
        }

        @Override
        public int nextIndex() {
            return index.get();
        }

        @Override
        public int previousIndex() {
            return index.get() - 1;
        }

        @Override
        public void remove() {
            set(null);
        }

        @Override
        public void set(T element) {
            setElement(index.get() - 1, element);
        }

        @Override
        public void setOpaque(T element) {
            MutableArray.this.setOpaque(index.get() - 1, element);
        }

        @Override
        public void setRelease(T element) {
            MutableArray.this.setRelease(index.get() - 1, element);
        }

        @Override
        public void setVolatile(T element) {
            MutableArray.this.setVolatile(index.get() - 1, element);
        }

        @Override
        public void add(T element) {
            set(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return MutableArray.this.getAndSet(index.get() - 1, element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index.get() - 1, element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index.get() - 1, element);
        }

        @Override
        public void previousRemove() {
            previousSet(null);
        }

        @Override
        public void previousSet(T element) {
            setElement(index.get(), element);
        }

        @Override
        public void previousSetOpaque(T element) {
            MutableArray.this.setOpaque(index.get(), element);
        }

        @Override
        public void previousSetRelease(T element) {
            MutableArray.this.setRelease(index.get(), element);
        }

        @Override
        public void previousSetVolatile(T element) {
            MutableArray.this.setVolatile(index.get(), element);
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousGetAndSet(T element) {
            return MutableArray.this.getAndSet(index.get(), element);
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index.get(), element);
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index.get(), element);
        }
    }

    class MutableSubArray implements SubArray<T> {
        private final int beginIndex;
        private final int endIndex;
        private final MutableSubArrayIterator iterator = new MutableSubArrayIterator();

        @VisibleForPerformance
        MutableSubArray(int beginIndex, int endIndex) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        MutableSubArrayIterator getIterator() {
            return iterator;
        }

        @Override
        public int getBeginIndex() {
            return beginIndex;
        }

        @Override
        public int getEndIndex() {
            return endIndex;
        }

        @Override
        public T[] getElements() {
            return MutableArray.this.getElements();
        }

        @Override
        public int getCapacity() {
            return endIndex - beginIndex;
        }

        @Override
        public int size() {
            int result = 0;
            for (int i = beginIndex; i < endIndex; i++) {
                if (elements[i] != null) {
                    result++;
                }
            }
            return result;
        }

        @Override
        public boolean isEmpty() {
            for (int i = beginIndex; i < endIndex; i++) {
                if (elements[i] != null) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean contains(Object element) {
            return indexOf(element) != -1;
        }

        @Override
        public Object[] toArray() {
            if (beginIndex == 0) {
                return Arrays.copyOf(elements, endIndex, elementsArrayClass);
            }
            return Arrays.copyOfRange(elements, beginIndex, endIndex, elementsArrayClass);
        }

        @Override
        public <U> U[] toArray(U[] array) {
            int capacity = getCapacity();
            if (array.length < capacity) {
                if (beginIndex == 0) {
                    return Arrays.copyOf(elements, endIndex, (Class<? extends U[]>) array.getClass());
                }
                return Arrays.copyOfRange(elements, beginIndex, endIndex, (Class<? extends U[]>) array.getClass());
            }
            System.arraycopy(elements, beginIndex, array, 0, capacity);
            return array;
        }

        @Override
        public boolean remove(Object element) {
            int i = indexOf(element);
            if (i == -1) {
                return false;
            }
            elements[i] = null;
            return true;
        }

        @Override
        public boolean containsAll(Collection<?> elements) {
            for (Object element : elements) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> elements) {
            if (index < 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            if (index + elements.size() > getCapacity()) {
                throw new ArrayIndexOutOfBoundsException(index);
            }

            index += beginIndex;
            for (T element : elements) {
                MutableArray.this.elements[index++] = element;
            }
            return !elements.isEmpty();
        }

        @Override
        public boolean removeAll(Collection<?> elements) {
            boolean changed = false;
            for (Object element : elements) {
                if (remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> elements) {
            boolean changed = false;
            for (int i = beginIndex; i < endIndex; i++) {
                if (!elements.contains(MutableArray.this.elements[i])) {
                    MutableArray.this.elements[i] = null;
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public void clear() {
            for (int i = beginIndex; i < endIndex; i++) {
                elements[i] = null;
            }
        }

        @Override
        public T get(int index) {
            return MutableArray.this.get(toSubIndex(index));
        }

        @Override
        public T getOpaque(int index) {
            return MutableArray.this.getOpaque(toSubIndex(index));
        }

        @Override
        public T getAcquire(int index) {
            return MutableArray.this.getAcquire(toSubIndex(index));
        }

        @Override
        public T getVolatile(int index) {
            return MutableArray.this.getVolatile(toSubIndex(index));
        }

        @Override
        public T set(int index, T element) {
            return MutableArray.this.set(toSubIndex(index), element);
        }

        @Override
        public void setElement(int index, T element) {
            MutableArray.this.setElement(toSubIndex(index), element);
        }

        @Override
        public void setOpaque(int index, T element) {
            MutableArray.this.setOpaque(toSubIndex(index), element);
        }

        @Override
        public void setRelease(int index, T element) {
            MutableArray.this.setRelease(toSubIndex(index), element);
        }

        @Override
        public void setVolatile(int index, T element) {
            MutableArray.this.setVolatile(toSubIndex(index), element);
        }

        @Override
        public void add(int index, T element) {
            setElement(index, element);
        }

        @Override
        public T remove(int index) {
            return set(index, null);
        }

        @Override
        public void removeElement(int index) {
            setElement(index, null);
        }

        @Override
        public int indexOf(Object element) {
            for (int i = beginIndex; i < endIndex; i++) {
                if (Objects.equals(element, elements[i])) {
                    return i - beginIndex;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object element) {
            for (int i = endIndex - 1; i >= beginIndex; i--) {
                if (Objects.equals(element, elements[i])) {
                    return i - beginIndex;
                }
            }
            return -1;
        }

        @Override
        public ArrayIterator<T> listIterator(int index) {
            iterator.initialize(beginIndex, endIndex, toSubIndex(index));
            return iterator;
        }

        @Override
        public SubArray<T> subList(int fromIndex, int toIndex) {
            return new MutableSubArray(toSubIndex(fromIndex), toSubIndex(toIndex));
        }

        @Override
        public boolean compareAndSet(int index, T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(int index, T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(toSubIndex(index), expectedElement, newElement);
        }

        @Override
        public T getAndSet(int index, T element) {
            return MutableArray.this.getAndSet(toSubIndex(index), element);
        }

        @Override
        public T getAndSetAcquire(int index, T element) {
            return MutableArray.this.getAndSetAcquire(toSubIndex(index), element);
        }

        @Override
        public T getAndSetRelease(int index, T element) {
            return MutableArray.this.getAndSetRelease(toSubIndex(index), element);
        }

        @Override
        public ArrayIterator<T> parallelIterator(int index) {
            return new MutableSubArrayIterator(beginIndex, endIndex, toSubIndex(index));
        }

        @Override
        public ArrayIterator<T> concurrentIterator(int index) {
            return new MutableSubArrayConcurrentIterator(beginIndex, endIndex, toSubIndex(index));
        }

        @Override
        public void fill(T value) {
            Arrays.fill(elements, beginIndex, endIndex, value);
        }

        @Override
        public void sort() {
            Arrays.sort(elements, beginIndex, endIndex);
        }

        @Override
        public void sort(Comparator<? super T> comparator) {
            Arrays.sort(elements, beginIndex, endIndex, comparator);
        }

        @Override
        public void parallelSort() {
            Arrays.parallelSort((Comparable[]) elements, beginIndex, endIndex);
        }

        @Override
        public void parallelSort(Comparator<? super T> comparator) {
            Arrays.parallelSort(elements, beginIndex, endIndex, comparator);
        }

        @Override
        public Stream<T> stream() {
            return Arrays.stream(elements, beginIndex, endIndex);
        }

        @Override
        public Stream<T> parallelStream() {
            return stream().parallel();
        }

        @Override
        public int binarySearch(T element) {
            return Arrays.binarySearch(elements, beginIndex, endIndex, element);
        }

        @Override
        public int binarySearch(T element, Comparator<? super T> comparator) {
            return Arrays.binarySearch(elements, beginIndex, endIndex, element, comparator);
        }

        @Override
        public void parallelPrefix(BinaryOperator<T> operator) {
            Arrays.parallelPrefix(elements, beginIndex, endIndex, operator);
        }

        @Override
        public void setAll(IntFunction<? extends T> generator) {
            for (int i = beginIndex; i < endIndex; i++) {
                elements[i] = generator.apply(i);
            }
        }

        @Override
        public void parallelSetAll(IntFunction<? extends T> generator) {
            IntStream.range(beginIndex, endIndex)
                    .parallel()
                    .forEach(i -> elements[i] = generator.apply(i));
        }

        @Override
        public int compareTo(Array<T> array) {
            int bFromIndex;
            int bToIndex;
            if (array instanceof SubArray<?>) {
                SubArray<T> subArray = (SubArray<T>) array;
                bFromIndex = subArray.getBeginIndex();
                bToIndex = subArray.getEndIndex();
            } else {
                bFromIndex = 0;
                bToIndex = array.getCapacity();
            }
            return Arrays.compare((Comparable[]) elements, beginIndex, endIndex, (Comparable[]) array.getElements(), bFromIndex, bToIndex);
        }

        public int mismatch(Array<T> array) {
            int bFromIndex;
            int bToIndex;
            if (array instanceof SubArray<?>) {
                SubArray<T> subArray = (SubArray<T>) array;
                bFromIndex = subArray.getBeginIndex();
                bToIndex = subArray.getEndIndex();
            } else {
                bFromIndex = 0;
                bToIndex = array.getCapacity();
            }
            return Arrays.mismatch(elements, beginIndex, endIndex, array.getElements(), bFromIndex, bToIndex);
        }

        public int mismatch(Array<T> array, Comparator<? super T> comparator) {
            int bFromIndex;
            int bToIndex;
            if (array instanceof SubArray<?>) {
                SubArray<T> subArray = (SubArray<T>) array;
                bFromIndex = subArray.getBeginIndex();
                bToIndex = subArray.getEndIndex();
            } else {
                bFromIndex = 0;
                bToIndex = array.getCapacity();
            }
            return Arrays.mismatch(elements, beginIndex, endIndex, array.getElements(), bFromIndex, bToIndex, comparator);
        }

        @Override
        public SubArray<T> unmodifiableView() {
            return view.new SubArrayView(this);
        }

        @Override
        public Array<T> immutableSnapshot() {
            return new ArrayView<>(clone());
        }

        @Override
        public Array<T> clone() {
            return MutableArray.fromCollection(this);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Array)) {
                return false;
            }
            Array<?> that = (Array<?>) object;
            if (getCapacity() != that.getCapacity()) {
                return false;
            }
            ArrayIterator<?> iterator = iterator();
            ArrayIterator<?> thatIterator = that.iterator();
            while (iterator.hasNext() && thatIterator.hasNext()) {
                if (!Objects.equals(iterator.next(), thatIterator.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i = beginIndex; i < endIndex; i++) {
                result = 31 * result + Objects.hashCode(elements[i]);
            }
            return result;
        }

        @Override
        public String toString() {
            int endIndexMinusOne = endIndex - 1;
            if (endIndexMinusOne == -1) {
                return "[]";
            }
            StringBuilder builder = new StringBuilder();
            builder.append('[');
            for (int i = beginIndex; ; i++) {
                builder.append(elements[i]);
                if (i == endIndexMinusOne) {
                    return builder.append(']').toString();
                }
                builder.append(", ");
            }
        }

        private int toSubIndex(int index) {
            if (index < 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            int result = beginIndex + index;
            if (result >= endIndex) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return result;
        }
    }

    class MutableSubArrayIterator implements SubArrayIterator<T> {
        private int beginIndex;
        private int endIndex;
        private int index;

        @VisibleForPerformance
        MutableSubArrayIterator() {}

        @VisibleForPerformance
        MutableSubArrayIterator(int beginIndex, int endIndex, int index) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.index = index;
        }

        @VisibleForPerformance
        void initialize(int beginIndex, int endIndex, int index) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.index = index;
        }

        @Override
        public int back() {
            if (hasPrevious()) {
                return --index;
            }
            throw new NoSuchElementException();
        }

        @Override
        public int forward() {
            if (hasNext()) {
                return index++;
            }
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public T next() {
            return get(forward());
        }

        @Override
        public T nextOpaque() {
            return getOpaque(forward());
        }

        @Override
        public T nextAcquire() {
            return getAcquire(forward());
        }

        @Override
        public T nextVolatile() {
            return getVolatile(forward());
        }

        @Override
        public boolean hasPrevious() {
            return index > beginIndex;
        }

        @Override
        public T previous() {
            return get(back());
        }

        @Override
        public T previousOpaque() {
            return getOpaque(back());
        }

        @Override
        public T previousAcquire() {
            return getAcquire(back());
        }

        @Override
        public T previousVolatile() {
            return getVolatile(back());
        }

        @Override
        public int nextIndex() {
            return index - beginIndex;
        }

        @Override
        public int previousIndex() {
            return index - beginIndex - 1;
        }

        @Override
        public int nextAbsoluteIndex() {
            return index;
        }

        @Override
        public int previousAbsoluteIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            set(null);
        }

        @Override
        public void set(T element) {
            setElement(index - 1, element);
        }

        @Override
        public void setOpaque(T element) {
            MutableArray.this.setOpaque(index - 1, element);
        }

        @Override
        public void setRelease(T element) {
            MutableArray.this.setRelease(index - 1, element);
        }

        @Override
        public void setVolatile(T element) {
            MutableArray.this.setVolatile(index - 1, element);
        }

        @Override
        public void add(T element) {
            set(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index - 1, expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return MutableArray.this.getAndSet(index - 1, element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index - 1, element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index - 1, element);
        }

        @Override
        public void previousRemove() {
            previousSet(null);
        }

        @Override
        public void previousSet(T element) {
            setElement(index, element);
        }

        @Override
        public void previousSetOpaque(T element) {
            MutableArray.this.setOpaque(index, element);
        }

        @Override
        public void previousSetRelease(T element) {
            MutableArray.this.setRelease(index, element);
        }

        @Override
        public void previousSetVolatile(T element) {
            MutableArray.this.setVolatile(index, element);
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index, expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index, expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index, expectedElement, newElement);
        }

        @Override
        public T previousGetAndSet(T element) {
            return MutableArray.this.getAndSet(index, element);
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index, element);
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index, element);
        }
    }

    private class MutableSubArrayConcurrentIterator implements SubArrayIterator<T> {
        private final int beginIndex;
        private final int endIndex;
        private final LazyVolatileInteger index;

        private final Lock lock = new ReentrantLock();

        MutableSubArrayConcurrentIterator(int beginIndex, int endIndex, int index) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.index = new LazyVolatileInteger(index);
        }

        @Override
        public int back() {
            if (hasPrevious()) {
                int newIndex = index.decrementAndGetPlain();
                lock.unlock();
                return newIndex;
            }
            lock.unlock();
            throw new NoSuchElementException();
        }

        @Override
        public int forward() {
            if (hasNext()) {
                int newIndex = index.getPlainAndIncrement();
                lock.unlock();
                return newIndex;
            }
            lock.unlock();
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasNext() {
            lock.lock();
            return index.getPlain() < endIndex;
        }

        @Override
        public T next() {
            return get(forward());
        }

        @Override
        public T nextOpaque() {
            return getOpaque(forward());
        }

        @Override
        public T nextAcquire() {
            return getAcquire(forward());
        }

        @Override
        public T nextVolatile() {
            return getVolatile(forward());
        }

        @Override
        public boolean hasPrevious() {
            lock.lock();
            return index.getPlain() > beginIndex;
        }

        @Override
        public T previous() {
            return get(back());
        }

        @Override
        public T previousOpaque() {
            return getOpaque(back());
        }

        @Override
        public T previousAcquire() {
            return getAcquire(back());
        }

        @Override
        public T previousVolatile() {
            return getVolatile(back());
        }

        @Override
        public int nextIndex() {
            return index.get() - beginIndex;
        }

        @Override
        public int previousIndex() {
            return index.get() - beginIndex - 1;
        }

        @Override
        public int nextAbsoluteIndex() {
            return index.get();
        }

        @Override
        public int previousAbsoluteIndex() {
            return index.get() - 1;
        }

        @Override
        public void remove() {
            set(null);
        }

        @Override
        public void set(T element) {
            setElement(index.get() - 1, element);
        }

        @Override
        public void setOpaque(T element) {
            MutableArray.this.setOpaque(index.get() - 1, element);
        }

        @Override
        public void setRelease(T element) {
            MutableArray.this.setRelease(index.get() - 1, element);
        }

        @Override
        public void setVolatile(T element) {
            MutableArray.this.setVolatile(index.get() - 1, element);
        }

        @Override
        public void add(T element) {
            set(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index.get() - 1, expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return MutableArray.this.getAndSet(index.get() - 1, element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index.get() - 1, element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index.get() - 1, element);
        }

        @Override
        public void previousRemove() {
            previousSet(null);
        }

        @Override
        public void previousSet(T element) {
            setElement(index.get(), element);
        }

        @Override
        public void previousSetOpaque(T element) {
            MutableArray.this.setOpaque(index.get(), element);
        }

        @Override
        public void previousSetRelease(T element) {
            MutableArray.this.setRelease(index.get(), element);
        }

        @Override
        public void previousSetVolatile(T element) {
            MutableArray.this.setVolatile(index.get(), element);
        }

        @Override
        public boolean previousCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.compareAndSet(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchange(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchange(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeAcquire(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeAcquire(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousCompareAndExchangeRelease(T expectedElement, T newElement) {
            return MutableArray.this.compareAndExchangeRelease(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetPlain(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetPlain(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSet(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSet(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetAcquire(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetAcquire(index.get(), expectedElement, newElement);
        }

        @Override
        public boolean previousWeakCompareAndSetRelease(T expectedElement, T newElement) {
            return MutableArray.this.weakCompareAndSetRelease(index.get(), expectedElement, newElement);
        }

        @Override
        public T previousGetAndSet(T element) {
            return MutableArray.this.getAndSet(index.get(), element);
        }

        @Override
        public T previousGetAndSetAcquire(T element) {
            return MutableArray.this.getAndSetAcquire(index.get(), element);
        }

        @Override
        public T previousGetAndSetRelease(T element) {
            return MutableArray.this.getAndSetRelease(index.get(), element);
        }
    }
}
