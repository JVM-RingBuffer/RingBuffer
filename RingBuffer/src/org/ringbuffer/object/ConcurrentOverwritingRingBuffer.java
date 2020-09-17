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

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Consumer;

@Contended
public class ConcurrentOverwritingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;

    private int readPosition;
    private int writePosition;
    private boolean isFull;

    @SuppressWarnings("unchecked")
    public ConcurrentOverwritingRingBuffer(int capacity) {
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        buffer = (T[]) new Object[capacity];
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition == readPosition) {
            if (isFull) {
                if (readPosition == 0) {
                    this.readPosition = capacityMinusOne;
                } else {
                    this.readPosition--;
                }
            } else {
                isFull = true;
            }
        }
        if (writePosition == 0) {
            this.writePosition = capacityMinusOne;
        } else {
            this.writePosition--;
        }
        buffer[writePosition] = element;
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        busyWaitStrategy.reset();
        while (true) {
            synchronized (this) {
                int readPosition = this.readPosition;
                if (writePosition != readPosition || isFull) {
                    isFull = false;
                    if (readPosition == 0) {
                        this.readPosition = capacityMinusOne;
                    } else {
                        this.readPosition--;
                    }
                    return buffer[readPosition];
                }
            }
            busyWaitStrategy.tick();
        }
    }

    @Override
    public synchronized int size() {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition < readPosition) {
            return readPosition - writePosition;
        }
        if (writePosition > readPosition) {
            return capacity - (writePosition - readPosition);
        }
        if (isFull) {
            return capacity;
        }
        return 0;
    }

    @Override
    public synchronized boolean isEmpty() {
        return writePosition == readPosition && !isFull;
    }

    @Override
    public void takeBatch(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takePlain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }
}
