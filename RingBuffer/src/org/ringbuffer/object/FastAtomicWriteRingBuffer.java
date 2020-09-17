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
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

@Contended
class FastAtomicWriteRingBuffer<T> extends FastRingBuffer<T> {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(FastAtomicWriteRingBuffer.class, "writePosition");

    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastAtomicWriteRingBuffer(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public void put(T element) {
        AtomicArray.setRelease(buffer, AtomicInt.getAndIncrementVolatile(this, WRITE_POSITION) & capacityMinusOne, element);
    }

    @Override
    public T take() {
        return take(readBusyWaitStrategy);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        T element;
        int readPosition = this.readPosition++ & capacityMinusOne;
        busyWaitStrategy.reset();
        while ((element = AtomicArray.getAndSetVolatile(buffer, readPosition, null)) == null) {
            busyWaitStrategy.tick();
        }
        return element;
    }
}
