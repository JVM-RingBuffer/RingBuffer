package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayTest {
    private static final String ZERO = "0";
    static final String ONE = "1";
    static final String TWO = "2";
    static final String THREE = "3";
    static final String FOUR = "4";
    static final String FIVE = "5";
    private static final String TWO_2 = "22";
    static final String[] ELEMENTS = {ZERO, ONE, null, TWO, ONE, THREE, null, null, FOUR};

    private final boolean isSubArray;

    AbstractArrayTest(boolean isSubArray) {
        this.isSubArray = isSubArray;
    }

    private final AbstractArray<String> array = getArray();
    private final AbstractArray<String> arrayFromCollection = getArrayFromCollection();
    private final AbstractArray<String> arrayWithRepeatedElements = getArrayWithRepeatedElements();
    private final AbstractArray<String> emptyArray = getEmptyArray();
    private final AbstractArray<String> emptyArrayFromCollection = getEmptyArrayFromCollection();

    abstract AbstractArray<String> getArray();

    abstract AbstractArray<String> getArrayFromCollection();

    abstract AbstractArray<String> getArrayWithRepeatedElements();

    abstract AbstractArray<String> getEmptyArray();

    abstract AbstractArray<String> getEmptyArrayFromCollection();

    @Test
    void size_isEmpty_getCapacity() {
        assertEquals(5, array.getCapacity());
        assertEquals(4, array.size());
        assertFalse(array.isEmpty());

        array.clear();
        assertEquals(5, array.getCapacity());
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());

        array.set(2, TWO);
        array.set(4, FOUR);
        array.set(1, ONE);
        assertEquals(5, array.getCapacity());
        assertEquals(3, array.size());
        assertFalse(array.isEmpty());

        array.remove(4);
        assertEquals(5, array.getCapacity());
        assertEquals(2, array.size());
        assertFalse(array.isEmpty());

        array.remove(ONE);
        assertEquals(5, array.getCapacity());
        assertEquals(1, array.size());
        assertFalse(array.isEmpty());

        Array<String> array = new Array<>(10);
        assertEquals(10, array.getCapacity());
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
    }

    @Test
    void contains() {
        assertTrue(array.contains(TWO));
        assertFalse(array.contains(FIVE));
    }

    @Test
    void iterator() {
        Iterator<String> iterator = array.iterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ONE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(TWO, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(THREE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(FOUR, iterator.next());
        assertFalse(iterator.hasNext());
        if (isSubArray) {
            assertEquals(FIVE, iterator.next());
        } else {
            assertThrows(ArrayIndexOutOfBoundsException.class, iterator::next);
        }
    }

    @Test
    void toArray() {
        Object[] elements = arrayWithRepeatedElements.toArray();
        assertArrayEquals(elements, ELEMENTS);
        assertNotSame(elements, ELEMENTS);
    }

    @Test
    void toTypedArray() {
        String[] elements = arrayWithRepeatedElements.toArray(new String[0]);
        assertArrayEquals(elements, ELEMENTS);
        assertNotSame(elements, ELEMENTS);
    }

    @Test
    void toTypedPresizedArray() {
        String[] elements = arrayWithRepeatedElements.toArray(new String[array.getCapacity()]);
        assertArrayEquals(elements, ELEMENTS);
        assertNotSame(elements, ELEMENTS);
    }

    @Test
    void removeObject() {
        assertTrue(array.remove(FOUR));
        assertNull(array.get(4));
    }

    @Test
    void containsAll() {
        assertTrue(array.containsAll(List.of(ONE, THREE, TWO)));
        assertFalse(array.containsAll(Set.of(ONE, FIVE)));
    }

    @Test
    void addAll() {
        final String three_2 = "33";
        final String four_2 = "44";
        assertTrue(array.addAll(2, List.of(TWO_2, three_2, four_2)));
        assertEquals(Array.of(null, ONE, TWO_2, three_2, four_2), array);
    }

    @Test
    void removeAll() {
        assertTrue(array.removeAll(Set.of(ZERO, FOUR)));
        assertEquals(Array.of(null, ONE, TWO, THREE, null), array);
    }

    @Test
    void retainAll() {
        assertTrue(array.retainAll(Array.of(ONE, THREE, FIVE)));
        assertEquals(Array.of(null, ONE, null, THREE, null), array);
    }

    @Test
    void clear() {
        array.clear();
        assertEquals(Array.empty(5), array);
    }

    @Test
    void set_get() {
        assertEquals(TWO, array.set(2, TWO_2));
        assertEquals(TWO_2, array.get(2));
    }

    @Test
    void setPlain_get() {
        array.setPlain(2, TWO_2);
        assertEquals(TWO_2, array.get(2));
    }

    @Test
    void removeIndex() {
        assertEquals(THREE, array.remove(3));
        assertNull(array.get(3));
    }

    @Test
    void removePlain() {
        array.removePlain(3);
        assertNull(array.get(3));
    }

    @Test
    void indexOf() {
        assertEquals(-1, arrayWithRepeatedElements.indexOf(FIVE));
        assertEquals(1, arrayWithRepeatedElements.indexOf(ONE));
    }

    @Test
    void lastIndexOf() {
        assertEquals(-1, arrayWithRepeatedElements.lastIndexOf(FIVE));
        assertEquals(4, arrayWithRepeatedElements.lastIndexOf(ONE));
    }

    @Test
    void listIterator() {
        ListIterator<String> iterator = array.listIterator();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());
        assertThrows(ArrayIndexOutOfBoundsException.class, iterator::previous);
        assertThrows(ArrayIndexOutOfBoundsException.class, iterator::next);
        assertNull(iterator.next());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());
        assertEquals(ONE, iterator.next());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());
        assertEquals(ONE, iterator.previous());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());
        assertEquals(ONE, iterator.next());

        assertEquals(TWO, iterator.next());
        assertEquals(THREE, iterator.next());
        assertEquals(FOUR, iterator.next());
        assertFalse(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(array.getCapacity(), iterator.nextIndex());
        assertEquals(4, iterator.previousIndex());
        if (isSubArray) {
            assertEquals(FIVE, iterator.next());
            assertEquals(FIVE, iterator.previous());
        } else {
            assertThrows(ArrayIndexOutOfBoundsException.class, iterator::next);
            assertThrows(ArrayIndexOutOfBoundsException.class, iterator::previous);
        }

        assertEquals(FOUR, iterator.previous());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(4, iterator.nextIndex());
        assertEquals(3, iterator.previousIndex());
    }

    @Test
    void listIteratorFromIndex() {
        ListIterator<String> iterator = array.listIterator(2);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());
        assertEquals(TWO, iterator.next());
        assertEquals(TWO, iterator.previous());
        assertEquals(ONE, iterator.previous());
    }

    @Test
    void testClone() {
        Array<String> clone = array.clone();
        assertNotSame(array, clone);
        assertEquals(array, clone);
    }

    @Test
    void equals() {
        assertEquals(array, arrayFromCollection);
        assertEquals(emptyArray, emptyArrayFromCollection);
    }

    @Test
    void testHashCode() {
        assertEquals(array.hashCode(), arrayFromCollection.hashCode());
        assertEquals(emptyArray.hashCode(), emptyArrayFromCollection.hashCode());
    }

    @Test
    void testToString() {
        final String arrayToString = "[null, 1, 2, 3, 4]";
        assertEquals(arrayToString, array.toString());
        assertEquals(arrayToString, arrayFromCollection.toString());
        final String emptyArrayToString = "[null, null, null, null, null]";
        assertEquals(emptyArrayToString, emptyArray.toString());
        assertEquals(emptyArrayToString, emptyArrayFromCollection.toString());
    }
}
