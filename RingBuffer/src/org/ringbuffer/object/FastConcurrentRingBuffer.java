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

package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

@Contended
class FastConcurrentRingBuffer<T> extends FastRingBuffer<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = FastConcurrentRingBuffer.class;
        READ_POSITION = Lang.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Lang.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacityMinusOne;
    private final T[] buffer;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastConcurrentRingBuffer(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public void put(T element) {
        AtomicArray.setRelease(buffer, AtomicInt.getAndIncrementVolatile(this, WRITE_POSITION) & capacityMinusOne, element);
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        T element;
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        busyWaitStrategy.reset();
        while ((element = AtomicArray.getAcquire(buffer, readPosition)) == null) {
            busyWaitStrategy.tick();
        }
        AtomicArray.setOpaque(buffer, readPosition, null);
        return element;
    }
}
