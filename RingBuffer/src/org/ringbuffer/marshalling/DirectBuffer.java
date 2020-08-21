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

package org.ringbuffer.marshalling;

import org.ringbuffer.lang.Assume;

import static org.ringbuffer.system.Unsafe.UNSAFE;

class DirectBuffer {
    static long allocate(long length) {
        Assume.notGreater(length, Long.MAX_VALUE - 8L);
        return UNSAFE.allocateMemory(length + 8L);
    }

    static void putByte(long address, long index, byte value) {
        UNSAFE.putByte(address + index, value);
    }

    static void putChar(long address, long index, char value) {
        UNSAFE.putChar(address + index, value);
    }

    static void putShort(long address, long index, short value) {
        UNSAFE.putShort(address + index, value);
    }

    static void putInt(long address, long index, int value) {
        UNSAFE.putInt(address + index, value);
    }

    static void putLong(long address, long index, long value) {
        UNSAFE.putLong(address + index, value);
    }

    static void putBoolean(long address, long index, boolean value) {
        UNSAFE.putBoolean(null, address + index, value);
    }

    static void putFloat(long address, long index, float value) {
        UNSAFE.putFloat(address + index, value);
    }

    static void putDouble(long address, long index, double value) {
        UNSAFE.putDouble(address + index, value);
    }

    static byte getByte(long address, long index) {
        return UNSAFE.getByte(address + index);
    }

    static char getChar(long address, long index) {
        return UNSAFE.getChar(address + index);
    }

    static short getShort(long address, long index) {
        return UNSAFE.getShort(address + index);
    }

    static int getInt(long address, long index) {
        return UNSAFE.getInt(address + index);
    }

    static long getLong(long address, long index) {
        return UNSAFE.getLong(address + index);
    }

    static boolean getBoolean(long address, long index) {
        return UNSAFE.getBoolean(null, address + index);
    }

    static float getFloat(long address, long index) {
        return UNSAFE.getFloat(address + index);
    }

    static double getDouble(long address, long index) {
        return UNSAFE.getDouble(address + index);
    }
}
