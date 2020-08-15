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
import org.ringbuffer.wait.BusyWaitStrategy;

public class DirectRingBufferBuilder extends AbstractDirectRingBufferBuilder<DirectRingBuffer> {
    DirectRingBufferBuilder(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public DirectRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder manyReaders() {
        super.manyReaders0();
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
    protected AbstractDirectRingBufferBuilder<?> withoutLocks() {
        throw new AssertionError();
    }

    @Override
    public DirectRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected DirectRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileDirectBlockingRingBuffer.class);
                        }
                        return new VolatileDirectBlockingRingBuffer(this);
                    case FAST:
                        return new FastVolatileDirectRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadDirectBlockingRingBuffer.class);
                        }
                        return new AtomicReadDirectBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicReadDirectRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteDirectBlockingRingBuffer.class);
                        }
                        return new AtomicWriteDirectBlockingRingBuffer(this);
                    case FAST:
                        return new FastAtomicWriteDirectRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentDirectBlockingRingBuffer.class);
                        }
                        return new ConcurrentDirectBlockingRingBuffer(this);
                    case FAST:
                        return new FastConcurrentDirectRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
