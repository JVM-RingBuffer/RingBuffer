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

/**
 * <pre>{@code
 * int key = ringBuffer.nextKey();
 * T element = ringBuffer.next(key);
 * // Populate element
 * ringBuffer.put(key);
 * }</pre>
 * <p>
 * If the ring buffer is not lock-free, then from {@link #nextKey()} to {@link #put(int)} is an atomic operation.
 */
public interface PrefilledRingBuffer<T> extends ObjectRingBuffer<T> {
    int nextKey();

    T next(int key);

    void put(int key);

    static <T> PrefilledRingBufferBuilder<T> withCapacity(int capacity) {
        return new PrefilledRingBufferBuilder<>(capacity);
    }
}
