package org.ringbuffer;

import eu.menzani.lang.Assert;

public abstract class RingBufferBuilderTest {
    protected static void expectClass(Class<?> clazz, Object... ringBuffers) {
        for (Object ringBuffer : ringBuffers) {
            Assert.equal(ringBuffer.getClass(), clazz);
        }
    }
}
