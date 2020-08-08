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
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;

abstract class FastConcurrentRingBuffer_pad0<T> extends FastRingBuffer<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastConcurrentRingBuffer_buf<T> extends FastConcurrentRingBuffer_pad0<T> {
    final int capacityMinusOne;
    final T[] buffer;

    FastConcurrentRingBuffer_buf(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
    }
}

abstract class FastConcurrentRingBuffer_pad1<T> extends FastConcurrentRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentRingBuffer_pad1(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentRingBuffer_read<T> extends FastConcurrentRingBuffer_pad1<T> {
    int readPosition;

    FastConcurrentRingBuffer_read(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentRingBuffer_pad2<T> extends FastConcurrentRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentRingBuffer_pad2(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentRingBuffer_write<T> extends FastConcurrentRingBuffer_pad2<T> {
    int writePosition;

    FastConcurrentRingBuffer_write(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentRingBuffer_pad3<T> extends FastConcurrentRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentRingBuffer_pad3(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

class FastConcurrentRingBuffer<T> extends FastConcurrentRingBuffer_pad3<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        READ_POSITION = Unsafe.objectFieldOffset(FastConcurrentRingBuffer_read.class, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(FastConcurrentRingBuffer_write.class, "writePosition");
    }

    FastConcurrentRingBuffer(RingBufferBuilder<T> builder) {
        super(builder);
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
        T element;
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        while ((element = AtomicArray.getAcquire(buffer, readPosition)) == null) {
            Thread.onSpinWait();
        }
        AtomicArray.setOpaque(buffer, readPosition, null);
        return element;
    }
}
