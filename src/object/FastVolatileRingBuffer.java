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

class FastVolatileRingBuffer<T> extends FastEmptyRingBuffer<T> {
    @Contended
    private final Object[] buffer;
    private final int capacityMinusOne;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastVolatileRingBuffer(EmptyRingBufferBuilder<?> builder) {
        buffer = builder.getBuffer();
        capacityMinusOne = builder.getCapacityMinusOne();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public void put(T element) {
        BUFFER.setRelease(buffer, writePosition++ & capacityMinusOne, element);
    }

    @Override
    public T take() {
        Object element;
        int readPosition = this.readPosition++ & capacityMinusOne;
        while ((element = BUFFER.getAcquire(buffer, readPosition)) == null) {
            Thread.onSpinWait();
        }
        buffer[readPosition] = null;
        return cast(element);
    }

    @SuppressWarnings("unchecked")
    private T cast(Object element) {
        return (T) element;
    }
}
