package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberTest {
    @Test
    void ceilDiv() {
        assertEquals(1, Number.ceilDiv(0, 5));
        assertEquals(1, Number.ceilDiv(1, 5));
        assertEquals(1, Number.ceilDiv(2, 5));
        assertEquals(1, Number.ceilDiv(3, 5));
        assertEquals(1, Number.ceilDiv(4, 5));
        assertEquals(2, Number.ceilDiv(6, 5));
        assertEquals(333_333 + 1, Number.ceilDiv(1_000_000, 3));

        assertThrows(ArithmeticException.class, () -> Number.ceilDiv(5, 0));
        assertEquals(5, Number.ceilDiv(5, 1));
        assertEquals(3, Number.ceilDiv(5, 2));
        assertEquals(2, Number.ceilDiv(5, 3));
        assertEquals(2, Number.ceilDiv(5, 4));
        assertEquals(1, Number.ceilDiv(5, 5));
        assertEquals(1, Number.ceilDiv(5, 6));
        assertEquals(1, Number.ceilDiv(5, 50));
    }

    @Test
    void getNextPowerOfTwo() {
        assertEquals(128, Number.getNextPowerOfTwo(128));
        assertEquals(256, Number.getNextPowerOfTwo(129));

        assertEquals(0, Number.getNextPowerOfTwo(-128));
        assertEquals(0, Number.getNextPowerOfTwo(-129));
    }

    @Test
    void isPowerOfTwo() {
        assertTrue(Number.isPowerOfTwo(128));
        assertFalse(Number.isPowerOfTwo(127));
        assertTrue(Number.isPowerOfTwo(0));
        assertTrue(Number.isPowerOfTwo(2));

        assertFalse(Number.isPowerOfTwo(-128));
        assertFalse(Number.isPowerOfTwo(-127));
        assertFalse(Number.isPowerOfTwo(-2));
    }
}
