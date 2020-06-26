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
import org.ringbuffer.memory.Integer;

abstract class AbstractMarshallingRingBufferBuilder<T> extends AbstractBaseMarshallingRingBufferBuilder<T> {
    private final int capacity;
    private ByteArray.Factory byteArrayFactory = ByteArray.SAFE;
    // All fields are copied in <init>(AbstractMarshallingRingBufferBuilder<T>)

    AbstractMarshallingRingBufferBuilder(int capacity) {
        Assume.notLesser(capacity, 2);
        validateCapacityPowerOfTwo(capacity);
        this.capacity = capacity;
    }

    AbstractMarshallingRingBufferBuilder(AbstractMarshallingRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
        byteArrayFactory = builder.byteArrayFactory;
    }

    public abstract AbstractMarshallingRingBufferBuilder<T> withByteArray(ByteArray.Factory factory);

    void withByteArray0(ByteArray.Factory factory) {
        byteArrayFactory = factory;
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    ByteArray getBuffer() {
        return byteArrayFactory.newInstance(capacity);
    }

    Integer newCursor() {
        return memoryOrder.newInteger();
    }
}
