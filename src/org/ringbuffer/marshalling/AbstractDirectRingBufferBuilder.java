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

import org.ringbuffer.memory.Long;

abstract class AbstractDirectRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final long capacity;
    private DirectByteArray.Factory byteArrayFactory = DirectByteArray.SAFE;
    private DirectAtomicBooleanArray.Factory writtenPositionsFactory = DirectAtomicBooleanArray.SAFE;
    // All fields are copied in <init>(AbstractDirectRingBufferBuilder<?>)

    AbstractDirectRingBufferBuilder(long capacity) {
        validateCapacity(capacity);
        validateCapacityPowerOfTwo(capacity);
        this.capacity = capacity;
    }

    AbstractDirectRingBufferBuilder(AbstractDirectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
        byteArrayFactory = builder.byteArrayFactory;
        writtenPositionsFactory = builder.writtenPositionsFactory;
    }

    /**
     * Require {@code --add-opens java.base/jdk.internal.misc=org.ringbuffer}.
     */
    @Override
    protected abstract AbstractDirectRingBufferBuilder<?> withoutLocks();

    @Override
    protected void withoutLocks0() {
        super.withoutLocks0();
        validateCapacityPowerOfTwo(capacity);
    }

    public abstract AbstractDirectRingBufferBuilder<T> withByteArray(DirectByteArray.Factory factory);

    void withByteArray0(DirectByteArray.Factory factory) {
        byteArrayFactory = factory;
    }

    public abstract AbstractDirectRingBufferBuilder<T> withWrittenPositions(DirectAtomicBooleanArray.Factory factory);

    void withWrittenPositions0(DirectAtomicBooleanArray.Factory factory) {
        writtenPositionsFactory = factory;
    }

    long getCapacity() {
        return capacity;
    }

    long getCapacityMinusOne() {
        return capacity - 1L;
    }

    DirectByteArray getBuffer() {
        return byteArrayFactory.newInstance(capacity);
    }

    Long newCursor() {
        return memoryOrder.newLong();
    }

    DirectAtomicBooleanArray getWrittenPositions() {
        DirectAtomicBooleanArray writtenPositions = writtenPositionsFactory.newInstance(capacity);
        writtenPositions.fill(true, capacity);
        return writtenPositions;
    }
}
