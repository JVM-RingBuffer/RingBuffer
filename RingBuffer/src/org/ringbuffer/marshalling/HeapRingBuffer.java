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

/**
 * If the ring buffer is not lock-free, then from {@link #next(int)} to {@link #put(int)} and from
 * {@link #take(int)} to {@link #advance(int)} is an atomic operation.
 */
public interface HeapRingBuffer extends AbstractHeapRingBuffer {
    int next(int size);

    /**
     * If the ring buffer is lock-free, then this method must not be called.
     */
    void advance(int offset);

    static HeapClearingRingBufferBuilder withCapacity(int capacity) {
        return new HeapClearingRingBufferBuilder(capacity);
    }
}
