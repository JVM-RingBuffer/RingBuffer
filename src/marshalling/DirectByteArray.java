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

import org.ringbuffer.marshalling.array.SafeDirectByteArray;
import org.ringbuffer.marshalling.array.UnsafeDirectByteArray;

public interface DirectByteArray {
    void putByte(long index, byte value);

    void putChar(long index, char value);

    void putShort(long index, short value);

    void putInt(long index, int value);

    void putLong(long index, long value);

    void putBoolean(long index, boolean value);

    void putFloat(long index, float value);

    void putDouble(long index, double value);

    byte getByte(long index);

    char getChar(long index);

    short getShort(long index);

    int getInt(long index);

    long getLong(long index);

    boolean getBoolean(long index);

    float getFloat(long index);

    double getDouble(long index);

    interface Factory {
        DirectByteArray newInstance(long capacity);
    }

    Factory SAFE = SafeDirectByteArray::new;
    Factory UNSAFE = UnsafeDirectByteArray::new;
}
