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

public class HeapMarshallingRingBufferBuilder extends AbstractHeapMarshallingRingBufferBuilder<MarshallingRingBuffer> {
    HeapMarshallingRingBufferBuilder(HeapMarshallingClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public HeapMarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder withReadLock(Lock lock) {
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
    public HeapMarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public HeapMarshallingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileHeapMarshallingBlockingRingBuffer.class);
                        }
                        return new VolatileHeapMarshallingBlockingRingBuffer(this);
                    case FAST:
                        return new FastVolatileHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadHeapMarshallingBlockingRingBuffer.class);
                        }
                        return new AtomicReadHeapMarshallingBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicReadHeapMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteHeapMarshallingBlockingRingBuffer.class);
                        }
                        return new AtomicWriteHeapMarshallingBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicWriteHeapMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentHeapMarshallingBlockingRingBuffer.class);
                        }
                        return new ConcurrentHeapMarshallingBlockingRingBuffer(this);
                    case FAST:
                        return new FastConcurrentHeapMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
