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

package org.ringbuffer;

import org.ringbuffer.classcopy.CopiedClass;
import org.ringbuffer.java.Assume;
import org.ringbuffer.java.Numbers;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.lock.ReentrantLock;
import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

import java.lang.invoke.MethodHandles;

public abstract class RingBufferBuilder<T> {
    private Boolean oneWriter;
    private Boolean oneReader;
    protected RingBufferType type = RingBufferType.CLEARING;
    private Lock writeLock;
    private Lock readLock;
    private BusyWaitStrategy writeBusyWaitStrategy;
    private BusyWaitStrategy readBusyWaitStrategy;
    protected MemoryOrder memoryOrder = MemoryOrder.LAZY;
    protected boolean copyClass;
    // All fields are copied in <init>(RingBufferBuilder<?>)

    protected RingBufferBuilder() {
    }

    protected RingBufferBuilder(RingBufferBuilder<?> builder) {
        oneWriter = builder.oneWriter;
        oneReader = builder.oneReader;
        type = builder.type;
        writeLock = builder.writeLock;
        readLock = builder.readLock;
        writeBusyWaitStrategy = builder.writeBusyWaitStrategy;
        readBusyWaitStrategy = builder.readBusyWaitStrategy;
        memoryOrder = builder.memoryOrder;
        copyClass = builder.copyClass;
    }

    public abstract RingBufferBuilder<T> oneWriter();

    protected void oneWriter0() {
        oneWriter = true;
    }

    public abstract RingBufferBuilder<T> manyWriters();

    protected void manyWriters0() {
        oneWriter = false;
    }

    public abstract RingBufferBuilder<T> oneReader();

    protected void oneReader0() {
        oneReader = true;
    }

    public abstract RingBufferBuilder<T> manyReaders();

    protected void manyReaders0() {
        oneReader = false;
    }

    public abstract RingBufferBuilder<T> withWriteLock(Lock lock);

    protected void withWriteLock0(Lock lock) {
        writeLock = lock;
    }

    public abstract RingBufferBuilder<T> withReadLock(Lock lock);

    protected void withReadLock0(Lock lock) {
        readLock = lock;
    }

    protected abstract RingBufferBuilder<?> blocking();

    protected void blocking0() {
        blocking0(HintBusyWaitStrategy.getDefault());
    }

    protected abstract RingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy);

    protected void blocking0(BusyWaitStrategy busyWaitStrategy) {
        type = RingBufferType.BLOCKING;
        writeBusyWaitStrategy = busyWaitStrategy;
    }

    protected abstract RingBufferBuilder<?> withoutLocks();

    protected void withoutLocks0() {
        type = RingBufferType.FAST;
    }

    public abstract RingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy);

    protected void waitingWith0(BusyWaitStrategy busyWaitStrategy) {
        readBusyWaitStrategy = busyWaitStrategy;
    }

    public abstract RingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder);

    protected void withMemoryOrder0(MemoryOrder memoryOrder) {
        this.memoryOrder = memoryOrder;
    }

    /**
     * A separate ring buffer implementation will be created to allow inlining of polymorphic calls.
     *
     * @see CopiedClass
     */
    public abstract RingBufferBuilder<T> copyClass();

    protected void copyClass0() {
        copyClass = true;
    }

    public T build() {
        validate();

        RingBufferConcurrency concurrency;
        if (oneReader) {
            if (oneWriter) {
                concurrency = RingBufferConcurrency.VOLATILE;
            } else {
                concurrency = RingBufferConcurrency.ATOMIC_WRITE;
            }
        } else if (oneWriter) {
            concurrency = RingBufferConcurrency.ATOMIC_READ;
        } else {
            concurrency = RingBufferConcurrency.CONCURRENT;
        }
        T ringBuffer = create(concurrency, type);
        afterBuild(ringBuffer);
        return ringBuffer;
    }

    protected void validate() {
        if (oneReader == null && oneWriter == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders(), and oneWriter() or manyWriters().");
        }
        if (oneReader == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders().");
        }
        if (oneWriter == null) {
            throw new IllegalStateException("You must call either oneWriter() or manyWriters().");
        }
    }

    protected abstract T create(RingBufferConcurrency concurrency, RingBufferType type);

    protected T instantiateCopy(Class<?> ringBufferClass) {
        return CopiedClass.<T>of(ringBufferClass, getImplLookup())
                .getConstructor(getClass())
                .call(this);
    }

    protected abstract MethodHandles.Lookup getImplLookup();

    protected Lock getWriteLock() {
        if (writeLock == null) {
            return defaultLock();
        }
        return writeLock;
    }

    protected Lock getReadLock() {
        if (readLock == null) {
            return defaultLock();
        }
        return readLock;
    }

    protected Lock defaultLock() {
        return new ReentrantLock();
    }

    protected BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    protected BusyWaitStrategy getReadBusyWaitStrategy() {
        if (readBusyWaitStrategy == null) {
            return HintBusyWaitStrategy.getDefault();
        }
        return readBusyWaitStrategy;
    }

    protected void afterBuild(T ringBuffer) {
    }

    protected static void validateCapacity(long capacity) {
        Assume.notLesser(capacity, 2L);
    }

    protected static void validateCapacityPowerOfTwo(long capacity) {
        if (!Numbers.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
    }

    protected enum RingBufferConcurrency {
        VOLATILE,
        ATOMIC_READ,
        ATOMIC_WRITE,
        CONCURRENT
    }

    protected enum RingBufferType {
        CLEARING,
        BLOCKING,
        DISCARDING,

        FAST
    }
}
