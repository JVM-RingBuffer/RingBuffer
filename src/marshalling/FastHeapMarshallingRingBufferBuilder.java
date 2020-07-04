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

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

public class FastHeapMarshallingRingBufferBuilder extends AbstractHeapMarshallingRingBufferBuilder<FastHeapMarshallingRingBuffer> {
    FastHeapMarshallingRingBufferBuilder(HeapMarshallingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder withWriteLock(Lock lock) {
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder withReadLock(Lock lock) {
        return this;
    }

    @Override
    protected AbstractRingBufferBuilder<?> blocking() {
        throw new AssertionError();
    }

    @Override
    protected AbstractRingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected AbstractRingBufferBuilder<?> fast() {
        throw new AssertionError();
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder copyClass() {
        return this;
    }

    @Override
    public FastHeapMarshallingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected FastHeapMarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.CLEARING_FAST) {
                    return new FastVolatileHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.CLEARING_FAST) {
                    return new FastAtomicReadHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.CLEARING_FAST) {
                    return new FastAtomicWriteHeapMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.CLEARING_FAST) {
                    return new FastConcurrentHeapMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
