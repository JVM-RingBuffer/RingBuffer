/*
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

import org.ringbuffer.wait.BusyWaitStrategy;

public class HeapClearingRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapClearingRingBuffer> {
    HeapClearingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public HeapClearingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder blocking() {
        super.blocking0();
        return new HeapRingBufferBuilder(this);
    }

    @Override
    public HeapRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new HeapRingBufferBuilder(this);
    }

    @Override
    public HeapRingBufferBuilder withoutLocks() {
        super.withoutLocks0();
        return new HeapRingBufferBuilder(this);
    }

    @Override
    public HeapClearingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected HeapClearingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileHeapRingBuffer.class);
                    }
                    return new VolatileHeapRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadHeapRingBuffer.class);
                    }
                    return new AtomicReadHeapRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteHeapRingBuffer.class);
                    }
                    return new AtomicWriteHeapRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.CLEARING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentHeapRingBuffer.class);
                    }
                    return new ConcurrentHeapRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
