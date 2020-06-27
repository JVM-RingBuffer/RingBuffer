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

import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

public class FastMarshallingRingBufferBuilder extends AbstractMarshallingRingBufferBuilder<FastMarshallingRingBuffer> {
    FastMarshallingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public FastMarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder withWriteLock(Lock lock) {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder withReadLock(Lock lock) {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder blocking() {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder copyClass() {
        return this;
    }

    @Override
    public FastMarshallingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected FastMarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:

            case ATOMIC_READ:

            case ATOMIC_WRITE:

            case CONCURRENT:
                return new FastConcurrentMarshallingRingBuffer(this);
        }
        throw new AssertionError();
    }
}
