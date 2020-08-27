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

import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

public interface ObjectRingBuffer<T> extends AbstractRingBuffer {
    int getCapacity();

    T take(BusyWaitStrategy busyWaitStrategy);

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled, then external synchronization
     * must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     T element = ringBuffer.take();
     *     // Read element
     * }
     * }</pre>
     */
    T take();

    /**
     * If the ring buffer supports at least one reader and writer, then this method allows to take
     * elements in batches. When it returns, at least {@code size} elements are available,
     * and {@link #takePlain()} must be invoked {@code size} times to take them all.
     * <p>
     * Moreover, if the ring buffer supports multiple readers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     ringBuffer.takeBatch(size);
     *     for (int i = size; i > 0; i--) {
     *         T element = ringBuffer.takePlain();
     *         // Read element
     *     }
     * }
     * }</pre>
     */
    void takeBatch(int size);

    /**
     * When taking elements in batches, this method must be called {@code size} times after
     * {@link #takeBatch(int) takeBatch(size)} has returned.
     */
    T takePlain();

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    void forEach(Consumer<T> action);

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean contains(T element);

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    int size();

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    @Override
    String toString();
}
