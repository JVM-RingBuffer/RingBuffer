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

public class HeapMarshallingBlockingRingBufferBuilder extends AbstractHeapMarshallingRingBufferBuilder<MarshallingBlockingRingBuffer> {
    HeapMarshallingBlockingRingBufferBuilder(HeapMarshallingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder withReadLock(Lock lock) {
        super.withReadLock0(lock);
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
    public HeapMarshallingBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public HeapMarshallingBlockingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileHeapMarshallingBlockingRingBuffer.class);
                    }
                    return new VolatileHeapMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadHeapMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicReadHeapMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteHeapMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicWriteHeapMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentHeapMarshallingBlockingRingBuffer.class);
                    }
                    return new ConcurrentHeapMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
