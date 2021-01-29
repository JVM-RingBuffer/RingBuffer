package org.ringbuffer;

import eu.menzani.lang.Assert;

public abstract class RingBufferBuilderTest {
    protected static void expectClass(Class<?> clazz, AbstractRingBuffer... ringBuffers) {
        for (AbstractRingBuffer ringBuffer : ringBuffers) {
            Assert.equal(ringBuffer.getClass(), clazz);
        }
    }
}
