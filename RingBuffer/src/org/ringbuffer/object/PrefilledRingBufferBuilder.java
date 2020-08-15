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

import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

public class PrefilledRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public PrefilledRingBufferBuilder<T> fillWith(Supplier<? extends T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> blocking() {
        super.blocking0();
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> discarding() {
        super.discarding0();
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> withoutLocks() {
        super.withoutLocks0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> copyClass() {
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
                    case FAST:
                        return new FastVolatilePrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadPrefilledRingBuffer.class);
                        }
                        return new AtomicReadPrefilledRingBuffer<>(this);
                    case FAST:
                        return new FastAtomicReadPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWritePrefilledRingBuffer.class);
                        }
                        return new AtomicWritePrefilledRingBuffer<>(this);
                    case FAST:
                        return new FastAtomicWritePrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case CLEARING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentPrefilledRingBuffer.class);
                        }
                        return new ConcurrentPrefilledRingBuffer<>(this);
                    case FAST:
                        return new FastConcurrentPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer<T> build() {
        return (PrefilledRingBuffer<T>) super.build();
    }
}
