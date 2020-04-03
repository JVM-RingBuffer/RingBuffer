package eu.menzani.ringbuffer;

import org.junit.jupiter.api.Test;
import perftest.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        assertEquals(AtomicReadBlockingRingBuffer.class, ManyReadersBlockingTest.RING_BUFFER.getClass());
        assertEquals(AtomicReadRingBuffer.class, ManyReadersTest.RING_BUFFER.getClass());

        assertEquals(AtomicWriteBlockingRingBuffer.class, ManyWritersBlockingTest.RING_BUFFER.getClass());
        assertEquals(AtomicWriteRingBuffer.class, ManyWritersTest.RING_BUFFER.getClass());

        assertEquals(VolatileBlockingRingBuffer.class, OneToOneBlockingTest.RING_BUFFER.getClass());
        assertEquals(VolatileRingBuffer.class, OneToOneTest.RING_BUFFER.getClass());

        assertEquals(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledManyReadersBlockingTest.RING_BUFFER.getClass());
        assertEquals(AtomicReadRingBuffer.class, PrefilledManyReadersTest.RING_BUFFER.getClass());

        assertEquals(AtomicWriteBlockingPrefilledRingBuffer.class, PrefilledManyWritersBlockingTest.RING_BUFFER.getClass());
        assertEquals(AtomicWriteRingBuffer.class, PrefilledManyWritersTest.RING_BUFFER.getClass());

        assertEquals(AtomicReadBlockingPrefilledRingBuffer.class, PrefilledOneToOneBlockingTest.RING_BUFFER.getClass());
        assertEquals(VolatileRingBuffer.class, PrefilledOneToOneTest.RING_BUFFER.getClass());

        assertEquals(LocalRingBuffer.class, LocalRingBufferTest.RING_BUFFER.getClass());
        assertEquals(LocalRingBuffer.class, PrefilledLocalRingBufferTest.RING_BUFFER.getClass());

        assertEquals(AtomicWriteBlockingRingBuffer.class, ProducersToProcessorToConsumersTest.PRODUCERS_RING_BUFFER.getClass());
        assertEquals(AtomicReadRingBuffer.class, ProducersToProcessorToConsumersTest.CONSUMERS_RING_BUFFER.getClass());
    }
}
