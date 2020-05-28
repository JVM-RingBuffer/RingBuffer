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
 * From {@link #next()} to {@link #put(int)} is an atomic operation.
 * From {@link #take(int)} to {@link #advance()} is an atomic operation.
 */
public interface MarshallingRingBuffer extends AbstractMarshallingRingBuffer {
    int next();

    void advance();

    static MarshallingRingBufferBuilder withCapacity(int capacity) {
        return new MarshallingRingBufferBuilder(capacity);
    }
}
