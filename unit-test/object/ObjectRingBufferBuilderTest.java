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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.AbstractRingBufferBuilderTest;
import test.object.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectRingBufferBuilderTest extends AbstractRingBufferBuilderTest {
    private RingBufferBuilder<?> builder;

    @BeforeEach
    void setUp() {
        builder = new RingBufferBuilder<>(2);
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

    @Test
    void testFillerNotSet() {
        AbstractRingBufferBuilder<?> builder = new PrefilledRingBufferBuilder<>(2);
        builder.oneReader();
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testClasses() {
        expectClass(ConcurrentBlockingGCRingBuffer.class, ManyToManyBlockingContentionTest.RING_BUFFER);
        expectClass(ConcurrentBlockingRingBuffer.class, ManyToManyBlockingTest.RING_BUFFER);
        expectClass(ConcurrentRingBuffer.class, ManyToManyContentionTest.RING_BUFFER);

        expectClass(AtomicReadBlockingGCRingBuffer.class, ManyReadersBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersContentionTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingGCRingBuffer.class, ManyWritersBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersContentionTest.RING_BUFFER);

        expectClass(VolatileBlockingGCRingBuffer.class, OneToOneBlockingContentionTest.RING_BUFFER);
        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneContentionTest.RING_BUFFER);

        expectClass(ConcurrentBlockingPrefilledRingBuffer.class, PrefilledManyToManyBlockingContentionTest.RING_BUFFER, PrefilledManyToManyBlockingTest.RING_BUFFER);
        expectClass(ConcurrentPrefilledRingBuffer.class, PrefilledManyToManyContentionTest.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingContentionTest.RING_BUFFER, PrefilledManyReadersBlockingTest.RING_BUFFER);
        expectClass(AtomicReadPrefilledRingBuffer.class, PrefilledManyReadersContentionTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingContentionTest.RING_BUFFER, PrefilledManyWritersBlockingTest.RING_BUFFER);
        expectClass(AtomicWritePrefilledRingBuffer.class, PrefilledManyWritersContentionTest.RING_BUFFER);

        expectClass(VolatileBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingContentionTest.RING_BUFFER, PrefilledOneToOneBlockingTest.RING_BUFFER);
        expectClass(VolatilePrefilledRingBuffer.class, PrefilledOneToOneContentionTest.RING_BUFFER);

        expectClass(FastAtomicWriteRingBuffer.class, ProducersToProcessorToConsumersContentionTest.PRODUCERS_RING_BUFFER);
        expectClass(FastAtomicReadPrefilledRingBuffer.class, ProducersToProcessorToConsumersContentionTest.CONSUMERS_RING_BUFFER);

        expectClass(FastConcurrentRingBuffer.class, FastManyToManyContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadRingBuffer.class, FastManyReadersContentionTest.RING_BUFFER);
        expectClass(FastAtomicWriteRingBuffer.class, FastManyWritersContentionTest.RING_BUFFER);
        expectClass(FastVolatileRingBuffer.class, FastOneToOneContentionTest.RING_BUFFER);

        expectClass(FastConcurrentPrefilledRingBuffer.class, FastPrefilledManyToManyContentionTest.RING_BUFFER);
        expectClass(FastAtomicReadPrefilledRingBuffer.class, FastPrefilledManyReadersContentionTest.RING_BUFFER);
        expectClass(FastAtomicWritePrefilledRingBuffer.class, FastPrefilledManyWritersContentionTest.RING_BUFFER);
        expectClass(FastVolatilePrefilledRingBuffer.class, FastPrefilledOneToOneContentionTest.RING_BUFFER);
    }
}
