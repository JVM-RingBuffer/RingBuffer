/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumbersTest {
    @Test
    void ceilDiv() {
        assertEquals(1, Numbers.ceilDiv(0, 5));
        assertEquals(1, Numbers.ceilDiv(1, 5));
        assertEquals(1, Numbers.ceilDiv(2, 5));
        assertEquals(1, Numbers.ceilDiv(3, 5));
        assertEquals(1, Numbers.ceilDiv(4, 5));
        assertEquals(2, Numbers.ceilDiv(6, 5));
        assertEquals(333_333 + 1, Numbers.ceilDiv(1_000_000, 3));

        assertThrows(ArithmeticException.class, () -> Numbers.ceilDiv(5, 0));
        assertEquals(5, Numbers.ceilDiv(5, 1));
        assertEquals(3, Numbers.ceilDiv(5, 2));
        assertEquals(2, Numbers.ceilDiv(5, 3));
        assertEquals(2, Numbers.ceilDiv(5, 4));
        assertEquals(1, Numbers.ceilDiv(5, 5));
        assertEquals(1, Numbers.ceilDiv(5, 6));
        assertEquals(1, Numbers.ceilDiv(5, 50));
    }

    @Test
    void getNextPowerOfTwo() {
        assertEquals(128, Numbers.getNextPowerOfTwo(128));
        assertEquals(256, Numbers.getNextPowerOfTwo(129));

        assertEquals(0, Numbers.getNextPowerOfTwo(-128));
        assertEquals(0, Numbers.getNextPowerOfTwo(-129));
    }

    @Test
    void isPowerOfTwo() {
        assertTrue(Numbers.isPowerOfTwo(128));
        assertFalse(Numbers.isPowerOfTwo(127));
        assertTrue(Numbers.isPowerOfTwo(0));
        assertTrue(Numbers.isPowerOfTwo(2));

        assertFalse(Numbers.isPowerOfTwo(-128));
        assertFalse(Numbers.isPowerOfTwo(-127));
        assertFalse(Numbers.isPowerOfTwo(-2));
    }
}
