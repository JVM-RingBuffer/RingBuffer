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

public class DirectMarshallingClearingRingBufferBuilder extends AbstractDirectMarshallingRingBufferBuilder<DirectMarshallingClearingRingBuffer> {
    DirectMarshallingClearingRingBufferBuilder(long capacity) {
        super(capacity);
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder withReadLock(Lock lock) {
        super.withReadLock0(lock);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder blocking() {
        super.blocking0();
        return new DirectMarshallingRingBufferBuilder(this);
    }

    @Override
    public DirectMarshallingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new DirectMarshallingRingBufferBuilder(this);
    }

    @Override
    public DirectMarshallingRingBufferBuilder fast() {
        super.fast0();
        return new DirectMarshallingRingBufferBuilder(this);
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder withByteArray(DirectByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    public DirectMarshallingClearingRingBufferBuilder withWrittenPositions(DirectAtomicBooleanArray.Factory factory) {
        super.withWrittenPositions0(factory);
        return this;
    }

    @Override
    protected DirectMarshallingClearingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileDirectMarshallingRingBuffer.class);
                    }
                    return new VolatileDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadDirectMarshallingRingBuffer.class);
                    }
                    return new AtomicReadDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteDirectMarshallingRingBuffer.class);
                    }
                    return new AtomicWriteDirectMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentDirectMarshallingRingBuffer.class);
                    }
                    return new ConcurrentDirectMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
