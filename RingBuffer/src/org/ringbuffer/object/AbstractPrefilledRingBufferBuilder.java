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

import org.ringbuffer.lang.Assume;

import java.util.function.Supplier;

abstract class AbstractPrefilledRingBufferBuilder<T> extends ObjectRingBufferBuilder<T> {
    private Supplier<? extends T> filler;
    // All fields are copied in <init>(AbstractPrefilledRingBufferBuilder<T>)

    AbstractPrefilledRingBufferBuilder(int capacity) {
        super(capacity);
    }

    AbstractPrefilledRingBufferBuilder(AbstractPrefilledRingBufferBuilder<T> builder) {
        super(builder);
        filler = builder.filler;
    }

    public abstract AbstractPrefilledRingBufferBuilder<T> fillWith(Supplier<? extends T> filler);

    void fillWith0(Supplier<? extends T> filler) {
        Assume.notNull(filler);
        this.filler = filler;
    }

    @Override
    protected void validate() {
        super.validate();
        if (filler == null) {
            throw new IllegalStateException("You must call fillWith().");
        }
    }

    @Override
    T[] getBuffer() {
        T[] buffer = super.getBuffer();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = filler.get();
        }
        return buffer;
    }

    T getDummyElement() {
        return filler.get();
    }
}
