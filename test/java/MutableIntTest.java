package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutableIntTest {
    @Test
    void ceilDiv() {
        assertEquals(1, MutableInt.ceilDiv(0, 5));
        assertEquals(1, MutableInt.ceilDiv(1, 5));
        assertEquals(1, MutableInt.ceilDiv(2, 5));
        assertEquals(1, MutableInt.ceilDiv(3, 5));
        assertEquals(1, MutableInt.ceilDiv(4, 5));
        assertEquals(2, MutableInt.ceilDiv(6, 5));
        assertEquals(333_333 + 1, MutableInt.ceilDiv(1_000_000, 3));

        assertThrows(ArithmeticException.class, () -> MutableInt.ceilDiv(5, 0));
        assertEquals(5, MutableInt.ceilDiv(5, 1));
        assertEquals(3, MutableInt.ceilDiv(5, 2));
        assertEquals(2, MutableInt.ceilDiv(5, 3));
        assertEquals(2, MutableInt.ceilDiv(5, 4));
        assertEquals(1, MutableInt.ceilDiv(5, 5));
        assertEquals(1, MutableInt.ceilDiv(5, 6));
        assertEquals(1, MutableInt.ceilDiv(5, 50));
    }
}
