package eu.menzani.ringbuffer.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntTest {
    @Test
    void ceilDiv() {
        assertEquals(1, Int.ceilDiv(0, 5));
        assertEquals(1, Int.ceilDiv(1, 5));
        assertEquals(1, Int.ceilDiv(2, 5));
        assertEquals(1, Int.ceilDiv(3, 5));
        assertEquals(1, Int.ceilDiv(4, 5));
        assertEquals(2, Int.ceilDiv(6, 5));
        assertEquals(333_333 + 1, Int.ceilDiv(1_000_000, 3));

        assertThrows(ArithmeticException.class, () -> Int.ceilDiv(5, 0));
        assertEquals(5, Int.ceilDiv(5, 1));
        assertEquals(3, Int.ceilDiv(5, 2));
        assertEquals(2, Int.ceilDiv(5, 3));
        assertEquals(2, Int.ceilDiv(5, 4));
        assertEquals(1, Int.ceilDiv(5, 5));
        assertEquals(1, Int.ceilDiv(5, 6));
        assertEquals(1, Int.ceilDiv(5, 50));
    }

    @Test
    void getNextPowerOfTwo() {
        assertEquals(128, Int.getNextPowerOfTwo(128));
        assertEquals(256, Int.getNextPowerOfTwo(129));

        assertEquals(0, Int.getNextPowerOfTwo(-128));
        assertEquals(0, Int.getNextPowerOfTwo(-129));
    }

    @Test
    void isPowerOfTwo() {
        assertTrue(Int.isPowerOfTwo(128));
        assertFalse(Int.isPowerOfTwo(127));
        assertTrue(Int.isPowerOfTwo(0));
        assertTrue(Int.isPowerOfTwo(2));

        assertFalse(Int.isPowerOfTwo(-128));
        assertFalse(Int.isPowerOfTwo(-127));
        assertFalse(Int.isPowerOfTwo(-2));
    }
}
