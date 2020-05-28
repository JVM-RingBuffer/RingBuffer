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
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

public class MarshallingBlockingRingBufferBuilder extends AbstractMarshallingRingBufferBuilder<MarshallingBlockingRingBuffer> {
    MarshallingBlockingRingBufferBuilder(MarshallingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public MarshallingBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder manyReaders() {
        super.manyReaders0();
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
    public MarshallingBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileMarshallingBlockingRingBuffer.class);
                    }
                    return new VolatileMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicReadMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicWriteMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentMarshallingBlockingRingBuffer.class);
                    }
                    return new ConcurrentMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
