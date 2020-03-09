package eu.menzani.ringbuffer.java;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class Array<T> implements AbstractArray<T>, Cloneable, Serializable {
    private static final Class<? extends Object[]> elementsArrayClass = Object[].class;
    private static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(elementsArrayClass);

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
    private RecycledIterator iterator;

    public Array(int capacity) {
        elements = new Object[capacity];
    }

    public Array(Collection<T> collection) {
        elements = collection.toArray();
    }

    public Array(T[] array) {
        elements = Arrays.copyOf(array, array.length, elementsArrayClass);
    }

    public void recycleIterator() {
        iterator = new RecycledIterator();
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
    public java.util.Iterator<T> iterator() {
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
    public boolean retainAll(Object... elements) {
        boolean changed = false;
        for (int i = 0; i < getCapacity(); i++) {
            if (arrayDoesNotContain(elements, this.elements[i])) {
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
        set(index, element);
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
            if (element.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object element) {
        for (int i = getCapacity() - 1; i >= 0; i--) {
            if (element.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return getIterator(0, getCapacity(), index);
    }

    private ListIterator<T> getIterator(int beginIndex, int endIndex, int index) {
        if (iterator == null) {
            return new ConcurrentIterator(beginIndex, endIndex, index);
        }
        iterator.recycle(beginIndex, endIndex, index);
        return iterator;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
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
    public Array<T> clone() {
        return new Array<>(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Array<?> that = (Array<?>) object;
        return Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    public interface Iterator<T> extends ListIterator<T> {
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

    private class RecycledIterator implements Iterator<T> {
        private int beginIndex;
        private int endIndex;
        private int index;

        void recycle(int beginIndex, int endIndex, int index) {
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
            if (hasNext()) {
                return get(index++);
            }
            throw new NoSuchElementException();
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
            if (hasPrevious()) {
                return get(--index);
            }
            throw new NoSuchElementException();
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
        public int nextAbsoluteIndex() {
            return nextIndex() - beginIndex;
        }

        @Override
        public int previousAbsoluteIndex() {
            return previousIndex() - beginIndex;
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
            throw new UnsupportedOperationException();
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

    private class ConcurrentIterator implements Iterator<T> {
        private final RecycledIterator delegate = new RecycledIterator();

        ConcurrentIterator(int beginIndex, int endIndex, int index) {
            delegate.recycle(beginIndex, endIndex, index);
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
        public boolean hasPrevious() {
            return delegate.hasPrevious();
        }

        @Override
        public T previous() {
            return delegate.previous();
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
            delegate.remove();
        }

        @Override
        public void set(T element) {
            delegate.set(element);
        }

        @Override
        public void setOpaque(T element) {
            delegate.setOpaque(element);
        }

        @Override
        public void setRelease(T element) {
            delegate.setRelease(element);
        }

        @Override
        public void setVolatile(T element) {
            delegate.setVolatile(element);
        }

        @Override
        public void add(T element) {
            delegate.add(element);
        }

        @Override
        public boolean compareAndSet(T expectedElement, T newElement) {
            return delegate.compareAndSet(expectedElement, newElement);
        }

        @Override
        public T compareAndExchange(T expectedElement, T newElement) {
            return delegate.compareAndExchange(expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeAcquire(T expectedElement, T newElement) {
            return delegate.compareAndExchangeAcquire(expectedElement, newElement);
        }

        @Override
        public T compareAndExchangeRelease(T expectedElement, T newElement) {
            return delegate.compareAndExchangeRelease(expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetPlain(T expectedElement, T newElement) {
            return delegate.weakCompareAndSetPlain(expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSet(T expectedElement, T newElement) {
            return delegate.weakCompareAndSet(expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetAcquire(T expectedElement, T newElement) {
            return delegate.weakCompareAndSetAcquire(expectedElement, newElement);
        }

        @Override
        public boolean weakCompareAndSetRelease(T expectedElement, T newElement) {
            return delegate.weakCompareAndSetRelease(expectedElement, newElement);
        }

        @Override
        public T getAndSet(T element) {
            return delegate.getAndSet(element);
        }

        @Override
        public T getAndSetAcquire(T element) {
            return delegate.getAndSetAcquire(element);
        }

        @Override
        public T getAndSetRelease(T element) {
            return delegate.getAndSetRelease(element);
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
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
        public java.util.Iterator<T> iterator() {
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
                Object[] result = new Object[capacity];
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
        public boolean retainAll(Object... elements) {
            boolean changed = false;
            for (int i = beginIndex; i < endIndex; i++) {
                if (arrayDoesNotContain(elements, Array.this.elements[i])) {
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
            set(beginIndex + index, element);
        }

        @Override
        public T remove(int index) {
            return set(beginIndex + index, null);
        }

        @Override
        public void removePlain(int index) {
            setPlain(beginIndex + index, null);
        }

        @Override
        public int indexOf(Object element) {
            for (int i = beginIndex; i < endIndex; i++) {
                if (element.equals(elements[i])) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object element) {
            for (int i = endIndex - 1; i >= beginIndex; i--) {
                if (element.equals(elements[i])) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public ListIterator<T> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            return getIterator(beginIndex, endIndex, index);
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
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
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            Array<?> thatArray = ((SubArray) object).getMainArray();
            if (Array.this.getCapacity() != thatArray.getCapacity()) {
                return false;
            }
            for (int i = beginIndex; i < endIndex; i++) {
                if (!Objects.equals(elements[i], thatArray.elements[i])) {
                    return false;
                }
            }
            return true;
        }

        private Array<?> getMainArray() {
            return Array.this;
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

    private static boolean arrayDoesNotContain(Object[] array, Object element) {
        for (Object arrayElement : array) {
            if (arrayElement.equals(element)) {
                return false;
            }
        }
        return true;
    }
}
