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

import java.util.function.Consumer;

@Contended
class AtomicWriteGCRingBuffer<T> implements RingBuffer<T> {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(AtomicWriteGCRingBuffer.class, "writePosition");

    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    @Contended
    private int writePosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicWriteGCRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition;
        AtomicArray.setPlain(buffer, writePosition, element);
        if (writePosition == 0) {
            AtomicInt.setRelease(this, WRITE_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, WRITE_POSITION, writePosition - 1);
        }
    }

    @Override
    public T take() {
        int readPosition = this.readPosition;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while (isEmptyCached(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            this.readPosition = capacityMinusOne;
        } else {
            this.readPosition--;
        }
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
    }

    private boolean isEmptyCached(int readPosition) {
        if (cachedWritePosition == readPosition) {
            cachedWritePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
            return cachedWritePosition == readPosition;
        }
        return false;
    }

    @Override
    public void takeBatch(int size) {
        int readPosition = this.readPosition;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        if (readPosition == 0) {
            readPosition = capacityMinusOne;
        } else {
            readPosition--;
        }
        return element;
    }

    @Override
    public T takeLast() {
        int position;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while ((position = AtomicInt.getAcquire(this, WRITE_POSITION)) == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (position == capacityMinusOne) {
            position = 0;
        } else {
            position++;
        }

        if (position <= readPosition) {
            for (int i = readPosition; i > position; i--) {
                AtomicArray.setPlain(buffer, i, null);
            }
        } else {
            takeLastSplit(position);
        }

        readPosition = position;
        return AtomicArray.getPlain(buffer, position);
    }

    private void takeLastSplit(int position) {
        for (int i = readPosition; i >= 0; i--) {
            AtomicArray.setPlain(buffer, i, null);
        }
        for (int i = capacityMinusOne; i > position; i--) {
            AtomicArray.setPlain(buffer, i, null);
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(AtomicArray.getPlain(buffer, i));
            }
        } else {
            forEachSplit(action, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(AtomicArray.getPlain(buffer, i));
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(AtomicArray.getPlain(buffer, i));
        }
    }

    @Override
    public boolean contains(T element) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (AtomicArray.getPlain(buffer, i).equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, writePosition);
    }

    private boolean containsSplit(T element, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            if (AtomicArray.getPlain(buffer, i).equals(element)) {
                return true;
            }
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            if (AtomicArray.getPlain(buffer, i).equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size(readPosition);
    }

    private int size(int readPosition) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(AtomicInt.getAcquire(this, WRITE_POSITION));
    }

    private boolean isEmpty(int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (isEmpty(writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(AtomicArray.getPlain(buffer, i).toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder, writePosition);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            builder.append(AtomicArray.getPlain(buffer, i).toString());
            builder.append(", ");
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            builder.append(AtomicArray.getPlain(buffer, i).toString());
            builder.append(", ");
        }
    }

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
