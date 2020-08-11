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

import org.ringbuffer.concurrent.DirectAtomicBooleanArray;
import org.ringbuffer.system.CleanerService;

abstract class AbstractDirectRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final long capacity;
    // All fields are copied in <init>(AbstractDirectRingBufferBuilder<?>)

    private transient final long[] memoryToFree = new long[2];

    AbstractDirectRingBufferBuilder(long capacity) {
        validateCapacity(capacity);
        validateCapacityPowerOfTwo(capacity);
        this.capacity = capacity;
    }

    AbstractDirectRingBufferBuilder(AbstractDirectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    @Override
    protected abstract AbstractDirectRingBufferBuilder<?> withoutLocks();

    @Override
    protected void withoutLocks0() {
        super.withoutLocks0();
        validateCapacityPowerOfTwo(capacity);
    }

    long getCapacity() {
        return capacity;
    }

    long getCapacityMinusOne() {
        return capacity - 1L;
    }

    long getBuffer() {
        long address = DirectBuffer.allocate(capacity);
        memoryToFree[0] = address;
        return address;
    }

    long getWrittenPositions() {
        long address = DirectAtomicBooleanArray.allocate(capacity);
        memoryToFree[1] = address;
        DirectAtomicBooleanArray.fill(address, capacity, true);
        return address;
    }

    @Override
    protected void afterBuild(T ringBuffer) {
        CleanerService.freeMemory(ringBuffer, memoryToFree);
    }
}
