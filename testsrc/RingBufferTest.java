package eu.menzani.ringbuffer;

import org.junit.Test;

import static org.junit.Assert.*;

abstract class RingBufferTest implements eu.menzani.ringbuffer.Test {
    final RingBuffer<Event> ringBuffer;

    RingBufferTest(RingBufferBuilder<Event> builder) {
        ringBuffer = builder.build();
    }

    @Test
    public void testClass() {
        assertEquals(getClazz(), ringBuffer.getClass());
    }

    abstract Class<?> getClazz();

    @Test
    public void testWriteAndRead() {
        runTest(getBenchmarkRepeatTimes(), getSum());
    }

    abstract int getBenchmarkRepeatTimes();

    abstract long getSum();
}
