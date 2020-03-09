package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTest {
    private final Array<String> array = Array.of(null, "1", "2", "3", "4");
    private final Array<String> arrayFromCollection = new Array<>(Arrays.asList(null, "1", "2", "3", "4"));

    private final String[] elements = {"0", "1", null, "2", "1", "3", null, null, "4"};
    private final Array<String> arrayWithRepeatedElements = Array.of(elements);

    private final Array<String> emptyArray = Array.empty(5);
    private final Array<String> emptyArrayFromCollection = new Array<>(Collections.nCopies(5, null));

    @Test
    void size_isEmpty_getCapacity() {
        assertEquals(5, array.getCapacity());
        assertEquals(4, array.size());
        assertFalse(array.isEmpty());

        array.clear();
        assertEquals(5, array.getCapacity());
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());

        array.set(2, "2");
        array.set(4, "4");
        array.set(1, "1");
        assertEquals(5, array.getCapacity());
        assertEquals(3, array.size());
        assertFalse(array.isEmpty());

        array.remove(4);
        assertEquals(5, array.getCapacity());
        assertEquals(2, array.size());
        assertFalse(array.isEmpty());

        array.remove("1");
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
        assertTrue(array.contains("2"));
        assertFalse(array.contains("5"));
    }

    @Test
    void iterator() {
        Iterator<String> iterator = array.iterator();
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("3", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("4", iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void toArray() {
        Object[] elements = arrayWithRepeatedElements.toArray();
        assertArrayEquals(elements, this.elements);
        assertNotSame(elements, this.elements);
    }

    @Test
    void toTypedArray() {
        String[] elements = arrayWithRepeatedElements.toArray(new String[0]);
        assertArrayEquals(elements, this.elements);
        assertNotSame(elements, this.elements);
    }

    @Test
    void toTypedPresizedArray() {
        String[] elements = arrayWithRepeatedElements.toArray(new String[array.getCapacity()]);
        assertArrayEquals(elements, this.elements);
        assertNotSame(elements, this.elements);
    }

    @Test
    void removeObject() {
        assertTrue(array.remove("4"));
        assertNull(array.get(4));
    }

    @Test
    void containsAll() {
        assertTrue(array.containsAll(List.of("1", "3", "2")));
        assertFalse(array.containsAll(Set.of("1", "5")));
    }

    @Test
    void addAll() {
        array.addAll(2, List.of("22", "33", "44"));
        assertEquals(Array.of(null, "1", "22", "33", "44"), array);
    }

    @Test
    void removeAll() {
        array.removeAll(Set.of("0", "4"));
        assertEquals(Array.of(null, "1", "2", "3", null), array);
    }

    @Test
    void retainAll() {
        assertTrue(array.retainAll("1", "3", "5"));
        assertEquals(Array.of(null, "1", null, "3", null), array);
    }

    @Test
    void clear() {
        array.clear();
        assertEquals(Array.empty(5), array);
    }

    @Test
    void set_get() {
        assertEquals("2", array.set(2, "22"));
        assertEquals("22", array.get(2));
    }

    @Test
    void setPlain_get() {
        array.setPlain(2, "22");
        assertEquals("22", array.get(2));
    }

    @Test
    void removeIndex() {
        assertEquals("3", array.remove(3));
        assertNull(array.get(3));
    }

    @Test
    void removePlain() {
        array.removePlain(3);
        assertNull(array.get(3));
    }

    @Test
    void indexOf() {
        assertEquals(-1, arrayWithRepeatedElements.indexOf("5"));
        assertEquals(1, arrayWithRepeatedElements.indexOf("1"));
    }

    @Test
    void lastIndexOf() {
        assertEquals(-1, arrayWithRepeatedElements.lastIndexOf("5"));
        assertEquals(4, arrayWithRepeatedElements.lastIndexOf("1"));
    }

    @Test
    void listIterator() {
        ListIterator<String> iterator = array.listIterator();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());
        assertThrows(NoSuchElementException.class, iterator::previous);
        assertNull(iterator.next());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());
        assertEquals("1", iterator.next());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());
        assertEquals("1", iterator.previous());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());
        assertEquals("1", iterator.next());

        assertEquals("2", iterator.next());
        assertEquals("3", iterator.next());
        assertEquals("4", iterator.next());
        assertFalse(iterator.hasNext());
        assertTrue(iterator.hasPrevious());
        assertEquals(array.getCapacity(), iterator.nextIndex());
        assertEquals(4, iterator.previousIndex());
        assertThrows(NoSuchElementException.class, iterator::next);

        assertEquals("4", iterator.previous());
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
        assertEquals("2", iterator.next());
        assertEquals("2", iterator.previous());
        assertEquals("1", iterator.previous());
    }

    @Test
    void recycledIterators() {
        array.recycleIterator();
        listIterator();
        listIterator();
        iterator();
        iterator();
        listIteratorFromIndex();
        listIteratorFromIndex();
    }

    @Test
    void subList() {
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
