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

import org.ringbuffer.marshalling.DirectByteArrayIndexOutOfBoundsException;

public class SafeDirectByteArray extends UnsafeDirectByteArray {
    private final long length;

    public SafeDirectByteArray(long length) {
        super(length);
        this.length = length;
    }

    private void checkBounds(long index) {
        if (index < 0L || index >= length) {
            throw new DirectByteArrayIndexOutOfBoundsException(index);
        }
    }

    @Override
    public void putByte(long index, byte value) {
        checkBounds(index);
        super.putByte(index, value);
    }

    @Override
    public void putChar(long index, char value) {
        checkBounds(index);
        super.putChar(index, value);
    }

    @Override
    public void putShort(long index, short value) {
        checkBounds(index);
        super.putShort(index, value);
    }

    @Override
    public void putInt(long index, int value) {
        checkBounds(index);
        super.putInt(index, value);
    }

    @Override
    public void putLong(long index, long value) {
        checkBounds(index);
        super.putLong(index, value);
    }

    @Override
    public void putBoolean(long index, boolean value) {
        checkBounds(index);
        super.putBoolean(index, value);
    }

    @Override
    public void putFloat(long index, float value) {
        checkBounds(index);
        super.putFloat(index, value);
    }

    @Override
    public void putDouble(long index, double value) {
        checkBounds(index);
        super.putDouble(index, value);
    }

    @Override
    public byte getByte(long index) {
        checkBounds(index);
        return super.getByte(index);
    }

    @Override
    public char getChar(long index) {
        checkBounds(index);
        return super.getChar(index);
    }

    @Override
    public short getShort(long index) {
        checkBounds(index);
        return super.getShort(index);
    }

    @Override
    public int getInt(long index) {
        checkBounds(index);
        return super.getInt(index);
    }

    @Override
    public long getLong(long index) {
        checkBounds(index);
        return super.getLong(index);
    }

    @Override
    public boolean getBoolean(long index) {
        checkBounds(index);
        return super.getBoolean(index);
    }

    @Override
    public float getFloat(long index) {
        checkBounds(index);
        return super.getFloat(index);
    }

    @Override
    public double getDouble(long index) {
        checkBounds(index);
        return super.getDouble(index);
    }
}
