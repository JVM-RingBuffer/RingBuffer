package org.ringbuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class RingBufferBuilderTest {
    protected static void expectClass(Class<?> clazz, AbstractRingBuffer... ringBuffers) {
        for (AbstractRingBuffer ringBuffer : ringBuffers) {
            assertEquals(clazz, ringBuffer.getClass());
        }
    }
}
