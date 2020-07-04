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

import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

public class PrefilledClearingRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledClearingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> fillWith(Supplier<? extends T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> withWriteLock(Lock lock) {
        super.withWriteLock0(lock);
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> withReadLock(Lock lock) {
        super.withReadLock0(lock);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> blocking() {
        super.blocking0();
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> discarding() {
        super.discarding0();
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> fast() {
        super.fast0();
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public PrefilledClearingRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected ObjectRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(VolatilePrefilledRingBuffer.class);
                        }
                        return new VolatilePrefilledRingBuffer<>(this);
                    case CLEARING_FAST:
                        return new FastVolatilePrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadPrefilledRingBuffer.class);
                        }
                        return new AtomicReadPrefilledRingBuffer<>(this);
                    case CLEARING_FAST:
                        return new FastAtomicReadPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWritePrefilledRingBuffer.class);
                        }
                        return new AtomicWritePrefilledRingBuffer<>(this);
                    case CLEARING_FAST:
                        return new FastAtomicWritePrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentPrefilledRingBuffer.class);
                        }
                        return new ConcurrentPrefilledRingBuffer<>(this);
                    case CLEARING_FAST:
                        return new FastConcurrentPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledClearingRingBuffer<T> build() {
        return (PrefilledClearingRingBuffer<T>) super.build();
    }
}
