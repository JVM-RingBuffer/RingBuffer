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

package org.ringbuffer.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.object.EmptyRingBufferBuilder;
import org.ringbuffer.object.PrefilledOverwritingRingBufferBuilder;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    private EmptyRingBufferBuilder<?> builder;

    @BeforeEach
    void setUp() {
        builder = new EmptyRingBufferBuilder<>(2);
    }

    @Test
    void testFillerNotSet() {
        AbstractRingBufferBuilder<?> builder = new PrefilledOverwritingRingBufferBuilder<>(2);
        builder.oneReader();
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testConcurrencyNotSet() {
        builder.blocking();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testWriterConcurrencyNotSet() {
        builder.oneReader();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testWriterConcurrencyNotSet2() {
        builder.manyReaders();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testReaderConcurrencyNotSet() {
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testReaderConcurrencyNotSet2() {
        builder.manyWriters();
        assertThrows(IllegalStateException.class, builder::build);
    }
}
