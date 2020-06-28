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
import org.ringbuffer.java.CleanerService;
import org.ringbuffer.marshalling.DirectByteArray;
import org.ringbuffer.system.Unsafe;

public class UnsafeDirectByteArray implements DirectByteArray {
    private final long address;

    public UnsafeDirectByteArray(long length) {
        Assume.notGreater(length, Long.MAX_VALUE - 8L);
        address = Unsafe.UNSAFE.allocateMemory(length + 8L);
        CleanerService.freeMemory(this, address);
    }

    @Override
    public void putByte(long index, byte value) {
        Unsafe.UNSAFE.putByte(null, address + index, value);
    }

    @Override
    public void putChar(long index, char value) {
        Unsafe.UNSAFE.putChar(null, address + index, value);
    }

    @Override
    public void putShort(long index, short value) {
        Unsafe.UNSAFE.putShort(null, address + index, value);
    }

    @Override
    public void putInt(long index, int value) {
        Unsafe.UNSAFE.putInt(null, address + index, value);
    }

    @Override
    public void putLong(long index, long value) {
        Unsafe.UNSAFE.putLong(null, address + index, value);
    }

    @Override
    public void putBoolean(long index, boolean value) {
        Unsafe.UNSAFE.putBoolean(null, address + index, value);
    }

    @Override
    public void putFloat(long index, float value) {
        Unsafe.UNSAFE.putFloat(null, address + index, value);
    }

    @Override
    public void putDouble(long index, double value) {
        Unsafe.UNSAFE.putDouble(null, address + index, value);
    }

    @Override
    public byte getByte(long index) {
        return Unsafe.UNSAFE.getByte(null, address + index);
    }

    @Override
    public char getChar(long index) {
        return Unsafe.UNSAFE.getChar(null, address + index);
    }

    @Override
    public short getShort(long index) {
        return Unsafe.UNSAFE.getShort(null, address + index);
    }

    @Override
    public int getInt(long index) {
        return Unsafe.UNSAFE.getInt(null, address + index);
    }

    @Override
    public long getLong(long index) {
        return Unsafe.UNSAFE.getLong(null, address + index);
    }

    @Override
    public boolean getBoolean(long index) {
        return Unsafe.UNSAFE.getBoolean(null, address + index);
    }

    @Override
    public float getFloat(long index) {
        return Unsafe.UNSAFE.getFloat(null, address + index);
    }

    @Override
    public double getDouble(long index) {
        return Unsafe.UNSAFE.getDouble(null, address + index);
    }
}
