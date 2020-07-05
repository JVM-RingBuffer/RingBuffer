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

class FastVolatileRingBuffer<T> extends FastRingBuffer<T> {
    private final int capacityMinusOne;
    @Contended
    private final AtomicArray<T> buffer;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastVolatileRingBuffer(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBufferArray();
    }

    @Override
    public int getCapacity() {
        return buffer.length();
    }

    @Override
    public void put(T element) {
        buffer.setRelease(writePosition++ & capacityMinusOne, element);
    }

    @Override
    public T take() {
        T element;
        int readPosition = this.readPosition++ & capacityMinusOne;
        while ((element = buffer.getAcquire(readPosition)) == null) {
            Thread.onSpinWait();
        }
        buffer.setPlain(readPosition, null);
        return element;
    }
}
