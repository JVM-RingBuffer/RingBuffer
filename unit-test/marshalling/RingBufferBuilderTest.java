package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBuffer;
import org.junit.jupiter.api.Test;
import test.marshalling.OneToOneMarshallingBlockingTest;
import test.marshalling.OneToOneMarshallingTest;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferBuilderTest {
    @Test
    void testClasses() {
        expectClass(VolatileMarshallingRingBuffer.class, OneToOneMarshallingTest.RING_BUFFER);
        expectClass(VolatileMarshallingBlockingRingBuffer.class, OneToOneMarshallingBlockingTest.RING_BUFFER);
    }

    private static void expectClass(Class<?> clazz, AbstractRingBuffer... ringBuffers) {
        for (AbstractRingBuffer ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
