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

class FastConcurrentPrefilledRingBuffer<T> extends FastPrefilledRingBuffer<T> {
    private final int capacityMinusOne;
    @Contended
    private final T[] buffer;
    @Contended
    private final AtomicBooleanArray writtenPositions;

    @Contended
    private final AtomicInt readPosition = new AtomicInt();
    @Contended
    private final AtomicInt writePosition = new AtomicInt();

    FastConcurrentPrefilledRingBuffer(PrefilledClearingRingBufferBuilder<T> builder) {
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
        return writePosition.getAndIncrementVolatile() & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return buffer[key];
    }

    @Override
    public void put(int key) {
        writtenPositions.setRelease(key, false);
    }

    @Override
    public T take() {
        int readPosition = this.readPosition.getAndIncrementVolatile() & capacityMinusOne;
        while (writtenPositions.getAcquire(readPosition)) {
            Thread.onSpinWait();
        }
        writtenPositions.setOpaque(readPosition, true);
        return buffer[readPosition];
    }
}
