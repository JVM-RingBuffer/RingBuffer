package org.ringbuffer.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ringbuffer.RingBufferBuilderTest;
import test.object.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectRingBufferBuilderTest extends RingBufferBuilderTest {
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
        ObjectRingBufferBuilder<?> builder = new PrefilledRingBufferBuilder<>(2);
        builder.oneReader();
        builder.oneWriter();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testClasses() {
        expectClass(ConcurrentBlockingRingBuffer.class, ManyToManyBlockingContentionTest.Holder.RING_BUFFER, ManyToManyBlockingContentionPerfTest.RING_BUFFER);
        expectClass(ConcurrentRingBuffer.class, ManyToManyContentionTest.Holder.RING_BUFFER);

        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingContentionTest.Holder.RING_BUFFER, ManyReadersBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersContentionTest.Holder.RING_BUFFER);

        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingContentionTest.Holder.RING_BUFFER, ManyWritersBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersContentionTest.Holder.RING_BUFFER);

        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingContentionTest.Holder.RING_BUFFER, OneToOneBlockingContentionPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneContentionTest.Holder.RING_BUFFER);

        expectClass(ConcurrentBlockingPrefilledRingBuffer.class, PrefilledManyToManyBlockingContentionTest.Holder.RING_BUFFER, PrefilledManyToManyBlockingContentionPerfTest.RING_BUFFER);
        expectClass(ConcurrentPrefilledRingBuffer.class, PrefilledManyToManyContentionTest.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingContentionTest.Holder.RING_BUFFER, PrefilledManyReadersBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicReadPrefilledRingBuffer.class, PrefilledManyReadersContentionTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingContentionTest.Holder.RING_BUFFER, PrefilledManyWritersBlockingContentionPerfTest.RING_BUFFER);
        expectClass(AtomicWritePrefilledRingBuffer.class, PrefilledManyWritersContentionTest.RING_BUFFER);

        expectClass(VolatileBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingContentionTest.Holder.RING_BUFFER, PrefilledOneToOneBlockingContentionPerfTest.RING_BUFFER);
        expectClass(VolatilePrefilledRingBuffer.class, PrefilledOneToOneContentionTest.RING_BUFFER);

        expectClass(LockfreeAtomicWriteRingBuffer.class, ProducersToProcessorToConsumersContentionTest.PRODUCERS_RING_BUFFER);
        expectClass(LockfreeAtomicReadPrefilledRingBuffer.class, ProducersToProcessorToConsumersContentionTest.CONSUMERS_RING_BUFFER);

        expectClass(LockfreeConcurrentRingBuffer.class, LockfreeManyToManyContentionTest.Holder.RING_BUFFER);
        expectClass(LockfreeAtomicReadRingBuffer.class, LockfreeManyReadersContentionTest.Holder.RING_BUFFER);
        expectClass(LockfreeAtomicWriteRingBuffer.class, LockfreeManyWritersContentionTest.Holder.RING_BUFFER);
        expectClass(LockfreeVolatileRingBuffer.class, LockfreeOneToOneContentionTest.Holder.RING_BUFFER);

        expectClass(LockfreeConcurrentPrefilledRingBuffer.class, LockfreePrefilledManyToManyContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicReadPrefilledRingBuffer.class, LockfreePrefilledManyReadersContentionTest.RING_BUFFER);
        expectClass(LockfreeAtomicWritePrefilledRingBuffer.class, LockfreePrefilledManyWritersContentionTest.RING_BUFFER);
        expectClass(LockfreeVolatilePrefilledRingBuffer.class, LockfreePrefilledOneToOneContentionTest.RING_BUFFER);
    }
}
