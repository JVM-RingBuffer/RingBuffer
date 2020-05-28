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
import org.ringbuffer.marshalling.DirectByteArray;

import static org.ringbuffer.system.Unsafe.*;

public class UnsafeDirectByteArray implements DirectByteArray {
    private final long address;

    public UnsafeDirectByteArray(long length) {
        Assume.notGreater(length, Long.MAX_VALUE - 8L);
        address = UNSAFE.allocateMemory(length + 8L);
    }

    @Override
    public void putByte(long index, byte value) {
        UNSAFE.putByte(null, address + index, value);
    }

    @Override
    public void putChar(long index, char value) {
        UNSAFE.putChar(null, address + index, value);
    }

    @Override
    public void putShort(long index, short value) {
        UNSAFE.putShort(null, address + index, value);
    }

    @Override
    public void putInt(long index, int value) {
        UNSAFE.putInt(null, address + index, value);
    }

    @Override
    public void putLong(long index, long value) {
        UNSAFE.putLong(null, address + index, value);
    }

    @Override
    public void putBoolean(long index, boolean value) {
        UNSAFE.putBoolean(null, address + index, value);
    }

    @Override
    public void putFloat(long index, float value) {
        UNSAFE.putFloat(null, address + index, value);
    }

    @Override
    public void putDouble(long index, double value) {
        UNSAFE.putDouble(null, address + index, value);
    }

    @Override
    public byte getByte(long index) {
        return UNSAFE.getByte(null, address + index);
    }

    @Override
    public char getChar(long index) {
        return UNSAFE.getChar(null, address + index);
    }

    @Override
    public short getShort(long index) {
        return UNSAFE.getShort(null, address + index);
    }

    @Override
    public int getInt(long index) {
        return UNSAFE.getInt(null, address + index);
    }

    @Override
    public long getLong(long index) {
        return UNSAFE.getLong(null, address + index);
    }

    @Override
    public boolean getBoolean(long index) {
        return UNSAFE.getBoolean(null, address + index);
    }

    @Override
    public float getFloat(long index) {
        return UNSAFE.getFloat(null, address + index);
    }

    @Override
    public double getDouble(long index) {
        return UNSAFE.getDouble(null, address + index);
    }
}
