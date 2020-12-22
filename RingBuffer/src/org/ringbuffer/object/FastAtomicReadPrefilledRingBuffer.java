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
import eu.menzani.atomic.AtomicBooleanArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

@Contended
class FastAtomicReadPrefilledRingBuffer<T> extends FastPrefilledRingBuffer<T> {
    private static final long READ_POSITION = Lang.objectFieldOffset(FastAtomicReadPrefilledRingBuffer.class, "readPosition");

    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean[] positionNotModified;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastAtomicReadPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        positionNotModified = builder.getPositionNotModified();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public int nextKey() {
        return writePosition++ & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return AtomicArray.getPlain(buffer, key);
    }

    @Override
    public void put(int key) {
        AtomicBooleanArray.setRelease(positionNotModified, key, false);
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        busyWaitStrategy.reset();
        while (AtomicBooleanArray.getAcquire(positionNotModified, readPosition)) {
            busyWaitStrategy.tick();
        }
        AtomicBooleanArray.setOpaque(positionNotModified, readPosition, true);
        return AtomicArray.getPlain(buffer, readPosition);
    }
}
