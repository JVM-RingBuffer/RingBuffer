package eu.menzani.ringbuffer.java;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * This collection, its sub-collections and iterators do not check bounds.
 */
public class Array<T> implements AbstractArray<T>, Serializable {
    private static final Array<?> empty = new Array<>(0);
    private static final Class<? extends Object[]> elementsArrayClass = Object[].class;
    private static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(elementsArrayClass);
    private static final VarHandle ITERATOR;
    private static final VarHandle VIEW;

    static {
        VarHandleLookup lookup = new VarHandleLookup(MethodHandles.lookup(), Array.class);
        ITERATOR = lookup.getVarHandle(Array.Iterator.class, "iterator");
        VIEW = lookup.getVarHandle(ArrayView.class, "view");
    }

    public static Array<?> empty() {
        return empty;
    }

    /**
     * This method is equivalent to the constructor {@link Array#Array(int)}.
     * Use this method to declare intent. Use the constructor when creating an array that will be populated.
     */
    public static <T> Array<T> empty(int capacity) {
        return new Array<>(capacity);
    }

    public static <T> Array<T> of(T element) {
        Array<T> result = new Array<>(1);
        result.setPlain(0, element);
        return result;
    }

    public static <T> Array<T> of(T first, T second) {
        Array<T> result = new Array<>(2);
        result.setPlain(0, first);
        result.setPlain(1, second);
        return result;
    }

    public static <T> Array<T> of(T first, T second, T third) {
        Array<T> result = new Array<>(3);
        result.setPlain(0, first);
        result.setPlain(1, second);
        result.setPlain(2, third);
        return result;
    }

    public static <T> Array<T> of(T first, T second, T third, T fourth) {
        Array<T> result = new Array<>(4);
        result.setPlain(0, first);
        result.setPlain(1, second);
        result.setPlain(2, third);
        result.setPlain(3, fourth);
        return result;
    }

    public static <T> Array<T> of(T first, T second, T third, T fourth, T fifth) {
        Array<T> result = new Array<>(5);
        result.setPlain(0, first);
        result.setPlain(1, second);
        result.setPlain(2, third);
        result.setPlain(3, fourth);
        result.setPlain(4, fifth);
        return result;
    }

    @SafeVarargs
    public static <T> Array<T> of(T... elements) {
        return new Array<>(elements);
    }

    private final Object[] elements;
    private Iterator iterator;
    private ArrayView<T> view;

    public Array(int capacity) {
        elements = new Object[capacity];
    }

    public Array(Collection<T> collection) {
        elements = collection.toArray();
    }

    public Array(T[] array) {
        elements = Arrays.copyOf(array, array.length, elementsArrayClass);
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
    public ArrayIterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, getCapacity(), elementsArrayClass);
    }

    @Override
    public <U> U[] toArray(U[] array) {
        int size = getCapacity();
        if (array.length < size) {
            Object[] result = Arrays.copyOf(elements, size, array.getClass());
            return (U[]) result;
        }
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    @Override
    public boolean add(T element) {
        throw new UnsupportedOperationException();
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
        for (Object element : elements) {
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
        Object element = elements[index];
        return (T) element;
    }

    @Override
    public T getOpaque(int index) {
        Object element = ELEMENTS.getOpaque(elements, index);
        return (T) element;
    }

    @Override
    public T getAcquire(int index) {
        Object element = ELEMENTS.getAcquire(elements, index);
        return (T) element;
    }

    @Override
    public T getVolatile(int index) {
        Object element = ELEMENTS.getVolatile(elements, index);
        return (T) element;
    }

    @Override
    public T set(int index, T element) {
        Object oldElement = elements[index];
        elements[index] = element;
        return (T) oldElement;
    }

    @Override
    public void setPlain(int index, T element) {
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
        setPlain(index, element);
    }

    @Override
    public T remove(int index) {
        return set(index, null);
    }

    @Override
    public void removePlain(int index) {
        setPlain(index, null);
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
    public ArrayIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ArrayIterator<T> listIterator(int index) {
        Iterator iterator = getIterator();
        iterator.initialize(0, getCapacity(), index);
        return iterator;
    }

    Iterator getIterator() {
        Iterator value = (Iterator) ITERATOR.getAcquire(this);
        if (value == null) {
            synchronized (this) {
                value = (Iterator) ITERATOR.getAcquire(this);
                if (value == null) {
                    value = new Iterator();
                    ITERATOR.setRelease(this, value);
                }
            }
        }
        return value;
    }

    @Override
    public AbstractArray<T> subList(int fromIndex, int toIndex) {
        return new SubArray(fromIndex, toIndex);
    }

    @Override
    public boolean compareAndSet(int index, T expectedElement, T newElement) {
        return ELEMENTS.compareAndSet(elements, index, expectedElement, newElement);
    }

    @Override
    public T compareAndExchange(int index, T expectedElement, T newElement) {
        Object oldElement = ELEMENTS.compareAndExchange(elements, index, expectedElement, newElement);
        return (T) oldElement;
    }

    @Override
    public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
        Object oldElement = ELEMENTS.compareAndExchangeAcquire(elements, index, expectedElement, newElement);
        return (T) oldElement;
    }

    @Override
    public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
        Object oldElement = ELEMENTS.compareAndExchangeRelease(elements, index, expectedElement, newElement);
        return (T) oldElement;
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
        Object oldElement = ELEMENTS.getAndSet(elements, index, element);
        return (T) oldElement;
    }

    @Override
    public T getAndSetAcquire(int index, T element) {
        Object oldElement = ELEMENTS.getAndSetAcquire(elements, index, element);
        return (T) oldElement;
    }

    @Override
    public T getAndSetRelease(int index, T element) {
        Object oldElement = ELEMENTS.getAndSetRelease(elements, index, element);
        return (T) oldElement;
    }

    @Override
    public AbstractArray<T> unmodifiableView() {
        return getView();
    }

    private ArrayView<T> getView() {
        ArrayView<T> value = (ArrayView<T>) VIEW.getAcquire(this);
        if (value == null) {
            synchronized (this) {
                value = (ArrayView<T>) VIEW.getAcquire(this);
                if (value == null) {
                    value = new ArrayView<>(this);
                    VIEW.setRelease(this, value);
                }
            }
        }
        return value;
    }

    @Override
    public AbstractArray<T> immutableSnapshot() {
        return new ArrayView<>(clone());
    }

    @Override
    public Array<T> clone() {
        return new Array<>(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractArray)) {
            return false;
        }
        AbstractArray<?> that = (AbstractArray<?>) object;
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

    private class Iterator implements ArrayIterator<T> {
        private int beginIndex;
        private int endIndex;
        private int index;

        void initialize(int beginIndex, int endIndex, int index) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.index = index;
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
            return index > beginIndex;
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
            return beginIndex + index;
        }

        @Override
        public int previousIndex() {
            return beginIndex + index - 1;
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
            setPlain(index, element);
        }

        @Override
        public void setOpaque(T element) {
            Array.this.setOpaque(index, element);
        }

        @Override
        public void setRelease(T element) {
            Array.this.setRelease(index, element);
        }

        @Override
        public void setVolatile(T element) {
            Array.this.setVolatile(index, element);
        }

        @Override
        public void add(T element) {
            set(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return Array.this.compareAndSet(index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return Array.this.compareAndExchange(index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return Array.this.compareAndExchangeAcquire(index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return Array.this.compareAndExchangeRelease(index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetPlain(index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return Array.this.weakCompareAndSet(index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetAcquire(index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetRelease(index, expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return Array.this.getAndSet(index, element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return Array.this.getAndSetAcquire(index, element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return Array.this.getAndSetRelease(index, element);
        }
    }

    private class SubArray implements AbstractArray<T> {
        private final int beginIndex;
        private final int endIndex;

        SubArray(int beginIndex, int endIndex) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
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
        public ArrayIterator<T> iterator() {
            return listIterator();
        }

        @Override
        public Object[] toArray() {
            if (beginIndex == 0) {
                return Arrays.copyOf(elements, endIndex, elementsArrayClass);
            }
            int capacity = getCapacity();
            Object[] result = new Object[capacity];
            System.arraycopy(elements, beginIndex, result, 0, capacity);
            return result;
        }

        @Override
        public <U> U[] toArray(U[] array) {
            int capacity = getCapacity();
            if (array.length < capacity) {
                if (beginIndex == 0) {
                    Object[] result = Arrays.copyOf(elements, endIndex, array.getClass());
                    return (U[]) result;
                }
                Object result = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), capacity);
                System.arraycopy(elements, beginIndex, result, 0, capacity);
                return (U[]) result;
            }
            System.arraycopy(elements, beginIndex, array, 0, capacity);
            return array;
        }

        @Override
        public boolean add(T element) {
            throw new UnsupportedOperationException();
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
            for (Object element : elements) {
                Array.this.elements[beginIndex + index++] = element;
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
                if (!elements.contains(Array.this.elements[i])) {
                    Array.this.elements[i] = null;
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
            return Array.this.get(beginIndex + index);
        }

        @Override
        public T getOpaque(int index) {
            return Array.this.getOpaque(beginIndex + index);
        }

        @Override
        public T getAcquire(int index) {
            return Array.this.getAcquire(beginIndex + index);
        }

        @Override
        public T getVolatile(int index) {
            return Array.this.getVolatile(beginIndex + index);
        }

        @Override
        public T set(int index, T element) {
            return Array.this.set(beginIndex + index, element);
        }

        @Override
        public void setPlain(int index, T element) {
            Array.this.setPlain(beginIndex + index, element);
        }

        @Override
        public void setOpaque(int index, T element) {
            Array.this.setOpaque(beginIndex + index, element);
        }

        @Override
        public void setRelease(int index, T element) {
            Array.this.setRelease(beginIndex + index, element);
        }

        @Override
        public void setVolatile(int index, T element) {
            Array.this.setVolatile(beginIndex + index, element);
        }

        @Override
        public void add(int index, T element) {
            setPlain(index, element);
        }

        @Override
        public T remove(int index) {
            return set(index, null);
        }

        @Override
        public void removePlain(int index) {
            setPlain(index, null);
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
        public ArrayIterator<T> listIterator() {
            return listIterator(0);
        }

        @Override
        public ArrayIterator<T> listIterator(int index) {
            Iterator iterator = getIterator();
            iterator.initialize(beginIndex, endIndex, beginIndex + index);
            return iterator;
        }

        @Override
        public AbstractArray<T> subList(int fromIndex, int toIndex) {
            return new SubArray(beginIndex + fromIndex, beginIndex + toIndex);
        }

        @Override
        public boolean compareAndSet(int index, T expectedElement, T newElement) {
            return Array.this.compareAndSet(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(int index, T expectedElement, T newElement) {
            return Array.this.compareAndExchange(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(int index, T expectedElement, T newElement) {
            return Array.this.compareAndExchangeAcquire(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(int index, T expectedElement, T newElement) {
            return Array.this.compareAndExchangeRelease(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(int index, T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetPlain(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(int index, T expectedElement, T newElement) {
            return Array.this.weakCompareAndSet(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(int index, T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetAcquire(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(int index, T expectedElement, T newElement) {
            return Array.this.weakCompareAndSetRelease(beginIndex + index, expectedElement, newElement);
        }

        @Override
        public T getAndSet(int index, T element) {
            return Array.this.getAndSet(beginIndex + index, element);
        }

        @Override
        public T getAndSetAcquire(int index, T element) {
            return Array.this.getAndSetAcquire(beginIndex + index, element);
        }

        @Override
        public T getAndSetRelease(int index, T element) {
            return Array.this.getAndSetRelease(beginIndex + index, element);
        }

        @Override
        public AbstractArray<T> unmodifiableView() {
            return getView().new SubArrayView(this);
        }

        @Override
        public AbstractArray<T> immutableSnapshot() {
            return getView().new SubArrayView(clone());
        }

        @Override
        public Array<T> clone() {
            return new Array<>(this);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof AbstractArray)) {
                return false;
            }
            AbstractArray<?> that = (AbstractArray<?>) object;
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
    }
}
