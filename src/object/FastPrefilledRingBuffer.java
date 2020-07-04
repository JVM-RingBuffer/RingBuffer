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

import java.util.function.Consumer;

abstract class FastPrefilledRingBuffer<T> implements PrefilledClearingRingBuffer<T> {
    @Override
    public void advance() {}

    @Override
    public void takeBatch(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takePlain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void advanceBatch() {
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
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }
}
