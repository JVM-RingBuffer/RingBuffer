package org.ringbuffer.marshalling;

import org.junit.jupiter.api.Test;
import org.ringbuffer.AbstractRingBuffer;
import test.marshalling.*;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(VolatileMarshallingRingBuffer.class, OneToOneMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileMarshallingBlockingRingBuffer.class, OneToOneMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingRingBuffer.class, OneToOneDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(VolatileDirectMarshallingBlockingRingBuffer.class, OneToOneDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicReadMarshallingRingBuffer.class, ManyReadersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadMarshallingBlockingRingBuffer.class, ManyReadersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingRingBuffer.class, ManyReadersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicReadDirectMarshallingBlockingRingBuffer.class, ManyReadersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(AtomicWriteMarshallingRingBuffer.class, ManyWritersMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteMarshallingBlockingRingBuffer.class, ManyWritersMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingRingBuffer.class, ManyWritersDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(AtomicWriteDirectMarshallingBlockingRingBuffer.class, ManyWritersDirectMarshallingBlockingContentionTest.RING_BUFFER);

        expectClass(ConcurrentMarshallingRingBuffer.class, ManyToManyMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentMarshallingBlockingRingBuffer.class, ManyToManyMarshallingBlockingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingRingBuffer.class, ManyToManyDirectMarshallingContentionTest.RING_BUFFER);
        expectClass(ConcurrentDirectMarshallingBlockingRingBuffer.class, ManyToManyDirectMarshallingBlockingContentionTest.RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, AbstractRingBuffer... ringBuffers) {
        for (AbstractRingBuffer ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
