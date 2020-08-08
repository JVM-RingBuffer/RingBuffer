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
import org.ringbuffer.concurrent.AtomicBooleanArray;
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;

abstract class FastConcurrentPrefilledRingBuffer_pad0<T> extends FastPrefilledRingBuffer<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastConcurrentPrefilledRingBuffer_buf<T> extends FastConcurrentPrefilledRingBuffer_pad0<T> {
    final int capacityMinusOne;
    final T[] buffer;
    final boolean[] writtenPositions;

    FastConcurrentPrefilledRingBuffer_buf(PrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }
}

abstract class FastConcurrentPrefilledRingBuffer_pad1<T> extends FastConcurrentPrefilledRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentPrefilledRingBuffer_pad1(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentPrefilledRingBuffer_read<T> extends FastConcurrentPrefilledRingBuffer_pad1<T> {
    int readPosition;

    FastConcurrentPrefilledRingBuffer_read(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentPrefilledRingBuffer_pad2<T> extends FastConcurrentPrefilledRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentPrefilledRingBuffer_pad2(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentPrefilledRingBuffer_write<T> extends FastConcurrentPrefilledRingBuffer_pad2<T> {
    int writePosition;

    FastConcurrentPrefilledRingBuffer_write(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastConcurrentPrefilledRingBuffer_pad3<T> extends FastConcurrentPrefilledRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastConcurrentPrefilledRingBuffer_pad3(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

class FastConcurrentPrefilledRingBuffer<T> extends FastConcurrentPrefilledRingBuffer_pad3<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        READ_POSITION = Unsafe.objectFieldOffset(FastConcurrentPrefilledRingBuffer_read.class, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(FastConcurrentPrefilledRingBuffer_write.class, "writePosition");
    }

    FastConcurrentPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public int nextKey() {
        return AtomicInt.getAndIncrementVolatile(this, WRITE_POSITION) & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return AtomicArray.getPlain(buffer, key);
    }

    @Override
    public void put(int key) {
        AtomicBooleanArray.setRelease(writtenPositions, key, false);
    }

    @Override
    public T take() {
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        while (AtomicBooleanArray.getAcquire(writtenPositions, readPosition)) {
            Thread.onSpinWait();
        }
        AtomicBooleanArray.setOpaque(writtenPositions, readPosition, true);
        return AtomicArray.getPlain(buffer, readPosition);
    }
}
