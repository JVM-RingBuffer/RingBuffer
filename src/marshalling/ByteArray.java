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

public interface ByteArray {
    void putByte(int index, byte value);

    void putChar(int index, char value);

    void putShort(int index, short value);

    void putInt(int index, int value);

    void putLong(int index, long value);

    void putBoolean(int index, boolean value);

    void putFloat(int index, float value);

    void putDouble(int index, double value);

    byte getByte(int index);

    char getChar(int index);

    short getShort(int index);

    int getInt(int index);

    long getLong(int index);

    boolean getBoolean(int index);

    float getFloat(int index);

    double getDouble(int index);

    interface Factory {
        ByteArray newInstance(int capacity);
    }
}
