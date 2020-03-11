package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayTest extends AbstractArrayViewTest {
    private static final String TWO_2 = "22";

    AbstractArrayTest(boolean isSubArray) {
        super(isSubArray);
    }

    @Test
    void size_isEmpty_getCapacity() {
        super.size_isEmpty_getCapacity();

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
    }

    @Test
    void removeObject() {
        assertTrue(array.remove(FOUR));
        assertNull(array.get(4));
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
}
