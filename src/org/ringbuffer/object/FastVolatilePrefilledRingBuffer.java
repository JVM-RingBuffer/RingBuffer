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

abstract class FastVolatilePrefilledRingBuffer_pad0<T> extends FastPrefilledRingBuffer<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastVolatilePrefilledRingBuffer_buf<T> extends FastVolatilePrefilledRingBuffer_pad0<T> {
    final int capacityMinusOne;
    final T[] buffer;
    final boolean[] writtenPositions;

    FastVolatilePrefilledRingBuffer_buf(PrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }
}

abstract class FastVolatilePrefilledRingBuffer_pad1<T> extends FastVolatilePrefilledRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatilePrefilledRingBuffer_pad1(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatilePrefilledRingBuffer_read<T> extends FastVolatilePrefilledRingBuffer_pad1<T> {
    int readPosition;

    FastVolatilePrefilledRingBuffer_read(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatilePrefilledRingBuffer_pad2<T> extends FastVolatilePrefilledRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatilePrefilledRingBuffer_pad2(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatilePrefilledRingBuffer_write<T> extends FastVolatilePrefilledRingBuffer_pad2<T> {
    int writePosition;

    FastVolatilePrefilledRingBuffer_write(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class FastVolatilePrefilledRingBuffer_pad3<T> extends FastVolatilePrefilledRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatilePrefilledRingBuffer_pad3(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

class FastVolatilePrefilledRingBuffer<T> extends FastVolatilePrefilledRingBuffer_pad3<T> {
    FastVolatilePrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public int nextKey() {
        return writePosition++ & capacityMinusOne;
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
        int readPosition = this.readPosition++ & capacityMinusOne;
        while (AtomicBooleanArray.getAcquire(writtenPositions, readPosition)) {
            Thread.onSpinWait();
        }
        AtomicBooleanArray.setPlain(writtenPositions, readPosition, true);
        return AtomicArray.getPlain(buffer, readPosition);
    }
}
