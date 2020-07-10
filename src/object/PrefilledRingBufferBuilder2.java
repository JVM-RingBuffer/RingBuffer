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

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

public class PrefilledRingBufferBuilder2<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder2(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> fillWith(Supplier<? extends T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> withReadLock(Lock lock) {
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
    ObjectRingBufferBuilder<?> discarding() {
        throw new AssertionError();
    }

    @Override
    protected AbstractRingBufferBuilder<?> fast() {
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBufferBuilder2<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected ObjectRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileBlockingPrefilledRingBuffer.class);
                        }
                        return new VolatileBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(VolatileDiscardingPrefilledRingBuffer.class);
                        }
                        return new VolatileDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadBlockingPrefilledRingBuffer.class);
                        }
                        return new AtomicReadBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadDiscardingPrefilledRingBuffer.class);
                        }
                        return new AtomicReadDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteBlockingPrefilledRingBuffer.class);
                        }
                        return new AtomicWriteBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteDiscardingPrefilledRingBuffer.class);
                        }
                        return new AtomicWriteDiscardingPrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentBlockingPrefilledRingBuffer.class);
                        }
                        return new ConcurrentBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentDiscardingPrefilledRingBuffer.class);
                        }
                        return new ConcurrentDiscardingPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer2<T> build() {
        return (PrefilledRingBuffer2<T>) super.build();
    }
}
