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

package org.ringbuffer.object;

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.concurrent.AtomicBooleanArray;
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;

class FastAtomicReadPrefilledRingBuffer<T> extends FastPrefilledRingBuffer<T> {
    private static final long READ_POSITION = Unsafe.objectFieldOffset(FastAtomicReadPrefilledRingBuffer.class, "readPosition");

    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean[] writtenPositions;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastAtomicReadPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public int nextKey() {
        return writePosition++ & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return buffer[key];
    }

    @Override
    public void put(int key) {
        AtomicBooleanArray.setRelease(writtenPositions, key, false);
    }

    @Override
    public T take() {
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        while (AtomicBooleanArray.getAndSetVolatile(writtenPositions, readPosition, true)) {
            Thread.onSpinWait();
        }
        return buffer[readPosition];
    }
}
