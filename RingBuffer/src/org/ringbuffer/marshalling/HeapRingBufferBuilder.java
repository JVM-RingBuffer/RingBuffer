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

import org.ringbuffer.RingBufferBuilder;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

public class HeapRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapRingBuffer> {
    HeapRingBufferBuilder(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public HeapRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public HeapRingBufferBuilder withReadLock(Lock lock) {
        super.withReadLock0(lock);
        return this;
    }

    @Override
    protected RingBufferBuilder<?> blocking() {
        throw new AssertionError();
    }

    @Override
    protected RingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected RingBufferBuilder<?> withoutLocks() {
        throw new AssertionError();
    }

    @Override
    public HeapRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected HeapRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileHeapBlockingRingBuffer.class);
                        }
                        return new VolatileHeapBlockingRingBuffer(this);
                    case FAST:
                        return new FastVolatileHeapRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadHeapBlockingRingBuffer.class);
                        }
                        return new AtomicReadHeapBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicReadHeapRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteHeapBlockingRingBuffer.class);
                        }
                        return new AtomicWriteHeapBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicWriteHeapRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentHeapBlockingRingBuffer.class);
                        }
                        return new ConcurrentHeapBlockingRingBuffer(this);
                    case FAST:
                        return new FastConcurrentHeapRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
