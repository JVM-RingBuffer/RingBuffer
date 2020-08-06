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

import org.ringbuffer.java.Assume;
import org.ringbuffer.memory.LongHandle;
import org.ringbuffer.system.CleanerService;
import org.ringbuffer.system.Unsafe;

abstract class AbstractDirectRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final long capacity;
    // All fields are copied in <init>(AbstractDirectRingBufferBuilder<?>)

    private transient final long[] memoryToFree = new long[2];

    AbstractDirectRingBufferBuilder(long capacity) {
        validateCapacity(capacity);
        validateCapacityPowerOfTwo(capacity);
        Assume.notGreater(capacity, Long.MAX_VALUE - 8L);
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
        long address = Unsafe.UNSAFE.allocateMemory(capacity + 8L);
        memoryToFree[0] = address;
        return address;
    }

    LongHandle newHandle() {
        return memoryOrder.newLongHandle();
    }

    long getWrittenPositions() {
        long address = Unsafe.UNSAFE.allocateMemory(capacity);
        memoryToFree[1] = address;
        Unsafe.UNSAFE.setMemory(address, capacity, (byte) 1); // Initialize all elements to `true`
        return address;
    }

    @Override
    protected void afterBuild(T ringBuffer) {
        CleanerService.freeMemory(ringBuffer, memoryToFree);
    }
}
