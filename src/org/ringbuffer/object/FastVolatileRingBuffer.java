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
import org.ringbuffer.concurrent.AtomicArray;

@Contended
class FastVolatileRingBuffer<T> extends FastRingBuffer<T> {
    private final int capacityMinusOne;
    private final T[] buffer;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastVolatileRingBuffer(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public void put(T element) {
        AtomicArray.setRelease(buffer, writePosition++ & capacityMinusOne, element);
    }

    @Override
    public T take() {
        T element;
        int readPosition = this.readPosition++ & capacityMinusOne;
        while ((element = AtomicArray.getAcquire(buffer, readPosition)) == null) {
            Thread.onSpinWait();
        }
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
    }
}
