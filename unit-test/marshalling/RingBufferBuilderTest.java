package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBuffer;
import org.junit.jupiter.api.Test;
import test.marshalling.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(VolatileMarshallingRingBuffer.class, OneToOneMarshallingTest.RING_BUFFER);
        expectClass(VolatileMarshallingBlockingRingBuffer.class, OneToOneMarshallingBlockingTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingRingBuffer.class, OneToOneDirectMarshallingTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingBlockingRingBuffer.class, OneToOneDirectMarshallingBlockingTest.RING_BUFFER);

        expectClass(AtomicReadMarshallingRingBuffer.class, ManyReadersMarshallingTest.RING_BUFFER);
        expectClass(AtomicReadMarshallingBlockingRingBuffer.class, ManyReadersMarshallingBlockingTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingRingBuffer.class, ManyReadersDirectMarshallingTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingBlockingRingBuffer.class, ManyReadersDirectMarshallingBlockingTest.RING_BUFFER);

        expectClass(AtomicWriteMarshallingRingBuffer.class, ManyWritersMarshallingTest.RING_BUFFER);
        expectClass(AtomicWriteMarshallingBlockingRingBuffer.class, ManyWritersMarshallingBlockingTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingRingBuffer.class, ManyWritersDirectMarshallingTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingBlockingRingBuffer.class, ManyWritersDirectMarshallingBlockingTest.RING_BUFFER);

        expectClass(ConcurrentMarshallingRingBuffer.class, ManyToManyMarshallingTest.RING_BUFFER);
        expectClass(ConcurrentMarshallingBlockingRingBuffer.class, ManyToManyMarshallingBlockingTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingRingBuffer.class, ManyToManyDirectMarshallingTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingBlockingRingBuffer.class, ManyToManyDirectMarshallingBlockingTest.RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, AbstractRingBuffer... ringBuffers) {
        for (AbstractRingBuffer ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
