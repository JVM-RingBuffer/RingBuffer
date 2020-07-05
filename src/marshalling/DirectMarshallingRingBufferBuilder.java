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

public class DirectMarshallingRingBufferBuilder extends AbstractDirectMarshallingRingBufferBuilder<DirectMarshallingRingBuffer> {
    DirectMarshallingRingBufferBuilder(DirectMarshallingClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public DirectMarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withReadLock(Lock lock) {
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
    protected AbstractDirectMarshallingRingBufferBuilder<?> fast() {
        throw new AssertionError();
    }

    @Override
    public DirectMarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withByteArray(DirectByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withWrittenPositions(DirectAtomicBooleanArray.Factory factory) {
        super.withWrittenPositions0(factory);
        return this;
    }

    @Override
    protected DirectMarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileDirectMarshallingBlockingRingBuffer.class);
                        }
                        return new VolatileDirectMarshallingBlockingRingBuffer(this);
                    case CLEARING_FAST:
                        return new FastVolatileDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadDirectMarshallingBlockingRingBuffer.class);
                        }
                        return new AtomicReadDirectMarshallingBlockingRingBuffer(this);
                    case CLEARING_FAST:
                        return new FastAtomicReadDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteDirectMarshallingBlockingRingBuffer.class);
                        }
                        return new AtomicWriteDirectMarshallingBlockingRingBuffer(this);
                    case CLEARING_FAST:
                        return new FastAtomicWriteDirectMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentDirectMarshallingBlockingRingBuffer.class);
                        }
                        return new ConcurrentDirectMarshallingBlockingRingBuffer(this);
                    case CLEARING_FAST:
                        return new FastConcurrentDirectMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
