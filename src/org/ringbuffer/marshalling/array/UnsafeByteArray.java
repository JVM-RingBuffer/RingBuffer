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

package org.ringbuffer.marshalling.array;

import org.ringbuffer.java.Assume;
import org.ringbuffer.marshalling.ByteArray;
import org.ringbuffer.system.Unsafe;

public class UnsafeByteArray implements ByteArray {
    private static final long base = Unsafe.UNSAFE.arrayBaseOffset(byte[].class);
    private static final long scale = Unsafe.UNSAFE.arrayIndexScale(byte[].class);

    private static long offset(int index) {
        return base + scale * index;
    }

    private final byte[] array;

    public UnsafeByteArray(int length) {
        Assume.notGreater(length, Integer.MAX_VALUE - 8);
        array = new byte[length + 8];
    }

    @Override
    public void putByte(int index, byte value) {
        Unsafe.UNSAFE.putByte(array, offset(index), value);
    }

    @Override
    public void putChar(int index, char value) {
        Unsafe.UNSAFE.putChar(array, offset(index), value);
    }

    @Override
    public void putShort(int index, short value) {
        Unsafe.UNSAFE.putShort(array, offset(index), value);
    }

    @Override
    public void putInt(int index, int value) {
        Unsafe.UNSAFE.putInt(array, offset(index), value);
    }

    @Override
    public void putLong(int index, long value) {
        Unsafe.UNSAFE.putLong(array, offset(index), value);
    }

    @Override
    public void putBoolean(int index, boolean value) {
        Unsafe.UNSAFE.putBoolean(array, offset(index), value);
    }

    @Override
    public void putFloat(int index, float value) {
        Unsafe.UNSAFE.putFloat(array, offset(index), value);
    }

    @Override
    public void putDouble(int index, double value) {
        Unsafe.UNSAFE.putDouble(array, offset(index), value);
    }

    @Override
    public byte getByte(int index) {
        return Unsafe.UNSAFE.getByte(array, offset(index));
    }

    @Override
    public char getChar(int index) {
        return Unsafe.UNSAFE.getChar(array, offset(index));
    }

    @Override
    public short getShort(int index) {
        return Unsafe.UNSAFE.getShort(array, offset(index));
    }

    @Override
    public int getInt(int index) {
        return Unsafe.UNSAFE.getInt(array, offset(index));
    }

    @Override
    public long getLong(int index) {
        return Unsafe.UNSAFE.getLong(array, offset(index));
    }

    @Override
    public boolean getBoolean(int index) {
        return Unsafe.UNSAFE.getBoolean(array, offset(index));
    }

    @Override
    public float getFloat(int index) {
        return Unsafe.UNSAFE.getFloat(array, offset(index));
    }

    @Override
    public double getDouble(int index) {
        return Unsafe.UNSAFE.getDouble(array, offset(index));
    }
}
