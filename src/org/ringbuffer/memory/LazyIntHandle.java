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

package org.ringbuffer.memory;

import org.ringbuffer.concurrent.AtomicInt;

class LazyIntHandle implements IntHandle {
    @Override
    public void set(Object instance, long offset, int value) {
        AtomicInt.setRelease(instance, offset, value);
    }

    @Override
    public int get(Object instance, long offset) {
        return AtomicInt.getAcquire(instance, offset);
    }
}
