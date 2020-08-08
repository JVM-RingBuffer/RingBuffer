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

import org.ringbuffer.concurrent.AtomicArray;

abstract class FastVolatileRingBuffer_pad0<T> extends FastRingBuffer<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastVolatileRingBuffer_buf<T> extends FastVolatileRingBuffer_pad0<T> {
    final int capacityMinusOne;
    final T[] buffer;

    FastVolatileRingBuffer_buf(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
    }
}

abstract class FastVolatileRingBuffer_pad1<T> extends FastVolatileRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileRingBuffer_pad1(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatileRingBuffer_read<T> extends FastVolatileRingBuffer_pad1<T> {
    int readPosition;

    FastVolatileRingBuffer_read(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatileRingBuffer_pad2<T> extends FastVolatileRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileRingBuffer_pad2(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatileRingBuffer_write<T> extends FastVolatileRingBuffer_pad2<T> {
    int writePosition;

    FastVolatileRingBuffer_write(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatileRingBuffer_pad3<T> extends FastVolatileRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileRingBuffer_pad3(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

class FastVolatileRingBuffer<T> extends FastVolatileRingBuffer_pad3<T> {
    FastVolatileRingBuffer(RingBufferBuilder<T> builder) {
        super(builder);
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
