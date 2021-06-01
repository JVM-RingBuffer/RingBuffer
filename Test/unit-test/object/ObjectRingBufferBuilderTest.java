package org.ringbuffer.object;

import bench.object.*;
import eu.menzani.lang.Assert;
import org.ringbuffer.RingBufferBuilderTest;

public class ObjectRingBufferBuilderTest extends RingBufferBuilderTest {
    private final RingBufferBuilder<?> builder = new RingBufferBuilder<>(2);

    public void testConcurrencyNotSet() {
        builder.blocking();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testWriterConcurrencyNotSet() {
        builder.oneReader();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testWriterConcurrencyNotSet2() {
        builder.manyReaders();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testReaderConcurrencyNotSet() {
        builder.oneWriter();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testReaderConcurrencyNotSet2() {
        builder.manyWriters();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testFillerNotSet() {
        ObjectRingBufferBuilder<?> builder = new PrefilledRingBufferBuilder<>(2);
        builder.oneReader();
        builder.oneWriter();
        Assert.fails(builder::build, IllegalStateException.class);
    }

    public void testClasses() {
        expectClass(ConcurrentBlockingRingBuffer.class, ManyToManyBlockingContentionBenchmark.Holder.RING_BUFFER, ManyToManyBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(ConcurrentRingBuffer.class, ManyToManyContentionBenchmark.Holder.RING_BUFFER);

        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingContentionBenchmark.Holder.RING_BUFFER, ManyReadersBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersContentionBenchmark.Holder.RING_BUFFER);

        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingContentionBenchmark.Holder.RING_BUFFER, ManyWritersBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersContentionBenchmark.Holder.RING_BUFFER);

        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingContentionBenchmark.Holder.RING_BUFFER, OneToOneBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneContentionBenchmark.Holder.RING_BUFFER);

        expectClass(ConcurrentBlockingPrefilledRingBuffer.class, PrefilledManyToManyBlockingContentionBenchmark.Holder.RING_BUFFER, PrefilledManyToManyBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(ConcurrentPrefilledRingBuffer.class, PrefilledManyToManyContentionBenchmark.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingContentionBenchmark.Holder.RING_BUFFER, PrefilledManyReadersBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicReadPrefilledRingBuffer.class, PrefilledManyReadersContentionBenchmark.RING_BUFFER);

        expectClass(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingContentionBenchmark.Holder.RING_BUFFER, PrefilledManyWritersBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(AtomicWritePrefilledRingBuffer.class, PrefilledManyWritersContentionBenchmark.RING_BUFFER);

        expectClass(VolatileBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingContentionBenchmark.Holder.RING_BUFFER, PrefilledOneToOneBlockingContentionPerfBenchmark.RING_BUFFER);
        expectClass(VolatilePrefilledRingBuffer.class, PrefilledOneToOneContentionBenchmark.RING_BUFFER);

        expectClass(LockfreeAtomicWriteRingBuffer.class, ProducersToProcessorToConsumersContentionBenchmark.PRODUCERS_RING_BUFFER);
        expectClass(LockfreeAtomicReadPrefilledRingBuffer.class, ProducersToProcessorToConsumersContentionBenchmark.CONSUMERS_RING_BUFFER);

        expectClass(LockfreeConcurrentRingBuffer.class, LockfreeManyToManyContentionBenchmark.Holder.RING_BUFFER);
        expectClass(LockfreeAtomicReadRingBuffer.class, LockfreeManyReadersContentionBenchmark.Holder.RING_BUFFER);
        expectClass(LockfreeAtomicWriteRingBuffer.class, LockfreeManyWritersContentionBenchmark.Holder.RING_BUFFER);
        expectClass(LockfreeVolatileRingBuffer.class, LockfreeOneToOneContentionBenchmark.Holder.RING_BUFFER);

        expectClass(LockfreeConcurrentPrefilledRingBuffer.class, LockfreePrefilledManyToManyContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicReadPrefilledRingBuffer.class, LockfreePrefilledManyReadersContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeAtomicWritePrefilledRingBuffer.class, LockfreePrefilledManyWritersContentionBenchmark.RING_BUFFER);
        expectClass(LockfreeVolatilePrefilledRingBuffer.class, LockfreePrefilledOneToOneContentionBenchmark.RING_BUFFER);
    }
}
