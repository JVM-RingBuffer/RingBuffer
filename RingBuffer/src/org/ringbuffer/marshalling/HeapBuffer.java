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

import static org.ringbuffer.UnsafeAccess.UNSAFE;
import static org.ringbuffer.concurrent.AtomicByteArray.elementOffset;

public class HeapBuffer {
    public static byte[] allocate(int length) {
        Assume.notGreater(length, Integer.MAX_VALUE - 8);
        return new byte[length + 8];
    }

    public static void putByte(byte[] array, int index, byte value) {
        UNSAFE.putByte(array, elementOffset(index), value);
    }

    public static void putChar(byte[] array, int index, char value) {
        UNSAFE.putChar(array, elementOffset(index), value);
    }

    public static void putShort(byte[] array, int index, short value) {
        UNSAFE.putShort(array, elementOffset(index), value);
    }

    public static void putInt(byte[] array, int index, int value) {
        UNSAFE.putInt(array, elementOffset(index), value);
    }

    public static void putLong(byte[] array, int index, long value) {
        UNSAFE.putLong(array, elementOffset(index), value);
    }

    public static void putBoolean(byte[] array, int index, boolean value) {
        UNSAFE.putBoolean(array, elementOffset(index), value);
    }

    public static void putFloat(byte[] array, int index, float value) {
        UNSAFE.putFloat(array, elementOffset(index), value);
    }

    public static void putDouble(byte[] array, int index, double value) {
        UNSAFE.putDouble(array, elementOffset(index), value);
    }

    public static byte getByte(byte[] array, int index) {
        return UNSAFE.getByte(array, elementOffset(index));
    }

    public static char getChar(byte[] array, int index) {
        return UNSAFE.getChar(array, elementOffset(index));
    }

    public static short getShort(byte[] array, int index) {
        return UNSAFE.getShort(array, elementOffset(index));
    }

    public static int getInt(byte[] array, int index) {
        return UNSAFE.getInt(array, elementOffset(index));
    }

    public static long getLong(byte[] array, int index) {
        return UNSAFE.getLong(array, elementOffset(index));
    }

    public static boolean getBoolean(byte[] array, int index) {
        return UNSAFE.getBoolean(array, elementOffset(index));
    }

    public static float getFloat(byte[] array, int index) {
        return UNSAFE.getFloat(array, elementOffset(index));
    }

    public static double getDouble(byte[] array, int index) {
        return UNSAFE.getDouble(array, elementOffset(index));
    }
}
