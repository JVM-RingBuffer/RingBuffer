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

import org.ringbuffer.AbstractRingBuffer;

interface AbstractDirectMarshallingRingBuffer extends AbstractRingBuffer {
    long getCapacity();

    void writeByte(long offset, byte value);

    void writeChar(long offset, char value);

    void writeShort(long offset, short value);

    void writeInt(long offset, int value);

    void writeLong(long offset, long value);

    void writeBoolean(long offset, boolean value);

    void writeFloat(long offset, float value);

    void writeDouble(long offset, double value);

    void put(long offset);

    long take(long size);

    byte readByte(long offset);

    char readChar(long offset);

    short readShort(long offset);

    int readInt(long offset);

    long readLong(long offset);

    boolean readBoolean(long offset);

    float readFloat(long offset);

    double readDouble(long offset);

    long size();
}
