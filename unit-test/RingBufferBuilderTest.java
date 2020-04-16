package eu.menzani.ringbuffer;

import org.junit.jupiter.api.Test;
import test.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(AtomicReadBlockingGCRingBuffer.class, ManyReadersBlockingTest.RING_BUFFER);
        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingGCRingBuffer.class, ManyWritersBlockingTest.RING_BUFFER);
        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersTest.RING_BUFFER);

        expectClass(VolatileBlockingGCRingBuffer.class, OneToOneBlockingTest.RING_BUFFER);
        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneTest.RING_BUFFER);

        expectClass(AtomicBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingTest.RING_BUFFER, PrefilledManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, PrefilledManyReadersTest.RING_BUFFER);

        expectClass(AtomicBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingTest.RING_BUFFER, PrefilledManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, PrefilledManyWritersTest.RING_BUFFER);

        expectClass(AtomicBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingTest.RING_BUFFER, PrefilledOneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, PrefilledOneToOneTest.RING_BUFFER);

        expectClass(LocalRingBuffer.class, LocalRingBufferTest.RING_BUFFER, PrefilledLocalRingBufferTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingGCRingBuffer.class, ProducersToProcessorToConsumersTest.PRODUCERS_RING_BUFFER);
        expectClass(AtomicWriteBlockingRingBuffer.class, ProducersToProcessorToConsumersPerfTest.PRODUCERS_RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ProducersToProcessorToConsumersTest.CONSUMERS_RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, RingBuffer<?>... ringBuffers) {
        for (RingBuffer<?> ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
