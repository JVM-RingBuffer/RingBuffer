package eu.menzani.ringbuffer;

import org.junit.jupiter.api.Test;
import perftest.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingTest.RING_BUFFER, ManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ManyReadersTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingTest.RING_BUFFER, ManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, ManyWritersTest.RING_BUFFER);

        expectClass(VolatileBlockingRingBuffer.class, OneToOneBlockingTest.RING_BUFFER, OneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, OneToOneTest.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingTest.RING_BUFFER, PrefilledManyReadersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, PrefilledManyReadersTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingTest.RING_BUFFER, PrefilledManyWritersBlockingPerfTest.RING_BUFFER);
        expectClass(AtomicWriteRingBuffer.class, PrefilledManyWritersTest.RING_BUFFER);

        expectClass(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingTest.RING_BUFFER, PrefilledOneToOneBlockingPerfTest.RING_BUFFER);
        expectClass(VolatileRingBuffer.class, PrefilledOneToOneTest.RING_BUFFER);

        expectClass(LocalRingBuffer.class, LocalRingBufferTest.RING_BUFFER);
        expectClass(LocalRingBuffer.class, PrefilledLocalRingBufferTest.RING_BUFFER);

        expectClass(AtomicWriteBlockingRingBuffer.class, ProducersToProcessorToConsumersTest.PRODUCERS_RING_BUFFER, ProducersToProcessorToConsumersPerfTest.PRODUCERS_RING_BUFFER);
        expectClass(AtomicReadRingBuffer.class, ProducersToProcessorToConsumersTest.CONSUMERS_RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, RingBuffer<?>... ringBuffers) {
        for (RingBuffer<?> ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
