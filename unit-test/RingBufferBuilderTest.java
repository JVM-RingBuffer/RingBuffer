package eu.menzani.ringbuffer;

import org.junit.jupiter.api.Test;
import test.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(ConcurrentBlockingGCRingBuffer.class, ManyToManyBlockingTest.RING_BUFFER);
        expectClass(ConcurrentBlockingRingBuffer.class, ManyToManyBlockingPerfTest.RING_BUFFER);
        expectClass(ConcurrentRingBuffer.class, ManyToManyTest.RING_BUFFER);

        expectClass(AtomicReadBlockingGCRingBuffer.class, ManyReadersBlockingTest.RING_BUFFER);
        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingGCRingBuffer.class, ManyWritersBlockingTest.RING_BUFFER);
        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersTest.RING_BUFFER);

        expectClass(VolatileBlockingGCRingBuffer.class, OneToOneBlockingTest.RING_BUFFER);
        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneTest.RING_BUFFER);

        expectClass(ConcurrentBlockingPrefilledRingBuffer.class, PrefilledManyToManyBlockingTest.RING_BUFFER, PrefilledManyToManyBlockingPerfTest.RING_BUFFER);
        expectClass(ConcurrentPrefilledRingBuffer.class, PrefilledManyToManyTest.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingTest.RING_BUFFER, PrefilledManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadPrefilledRingBuffer.class, PrefilledManyReadersTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingTest.RING_BUFFER, PrefilledManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicWritePrefilledRingBuffer.class, PrefilledManyWritersTest.RING_BUFFER);

        expectClass(VolatileBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingTest.RING_BUFFER, PrefilledOneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatilePrefilledRingBuffer.class, PrefilledOneToOneTest.RING_BUFFER);

        expectClass(LocalGCRingBuffer.class, LocalRingBufferTest.RING_BUFFER);
        expectClass(LocalPrefilledRingBuffer.class, PrefilledLocalRingBufferTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingGCRingBuffer.class, ProducersToProcessorToConsumersTest.PRODUCERS_RING_BUFFER);
        expectClass(AtomicWriteBlockingRingBuffer.class, ProducersToProcessorToConsumersPerfTest.PRODUCERS_RING_BUFFER);
        expectClass(AtomicReadPrefilledRingBuffer.class, ProducersToProcessorToConsumersTest.CONSUMERS_RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, RingBuffer<?>... ringBuffers) {
        for (RingBuffer<?> ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
