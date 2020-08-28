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
import org.ringbuffer.concurrent.ThreadLocal;
import org.ringbuffer.wait.BusyWaitStrategy;

interface AbstractHeapRingBuffer extends AbstractRingBuffer {
    int getCapacity();

    /**
     * If the ring buffer is lock-free, then {@code offset} is the value returned by
     * {@link HeapRingBuffer#next(int)}.
     */
    void put(int offset);

    int take(int size, @ThreadLocal BusyWaitStrategy busyWaitStrategy);

    /**
     * If the ring buffer supports multiple readers and is not lock-free, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     int offset = ringBuffer.take(...);
     *     // Read data
     *     ringBuffer.advance(...); // If needed
     * }
     * }</pre>
     */
    int take(int size);

    int size();

    void writeByte(int offset, byte value);

    void writeChar(int offset, char value);

    void writeShort(int offset, short value);

    void writeInt(int offset, int value);

    void writeLong(int offset, long value);

    void writeBoolean(int offset, boolean value);

    void writeFloat(int offset, float value);

    void writeDouble(int offset, double value);

    byte readByte(int offset);

    char readChar(int offset);

    short readShort(int offset);

    int readInt(int offset);

    long readLong(int offset);

    boolean readBoolean(int offset);

    float readFloat(int offset);

    double readDouble(int offset);
}
