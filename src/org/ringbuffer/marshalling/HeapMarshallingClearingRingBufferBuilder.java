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

public class HeapMarshallingClearingRingBufferBuilder extends AbstractHeapMarshallingRingBufferBuilder<MarshallingClearingRingBuffer> {
    HeapMarshallingClearingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder withReadLock(Lock lock) {
        super.withReadLock0(lock);
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder blocking() {
        super.blocking0();
        return new HeapMarshallingRingBufferBuilder(this);
    }

    @Override
    public HeapMarshallingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new HeapMarshallingRingBufferBuilder(this);
    }

    @Override
    public HeapMarshallingRingBufferBuilder fast() {
        super.fast0();
        return new HeapMarshallingRingBufferBuilder(this);
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public HeapMarshallingClearingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingClearingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileHeapMarshallingRingBuffer.class);
                    }
                    return new VolatileHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadHeapMarshallingRingBuffer.class);
                    }
                    return new AtomicReadHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteHeapMarshallingRingBuffer.class);
                    }
                    return new AtomicWriteHeapMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentHeapMarshallingRingBuffer.class);
                    }
                    return new ConcurrentHeapMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
