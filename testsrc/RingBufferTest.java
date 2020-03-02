package eu.menzani.ringbuffer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class RingBufferTest implements eu.menzani.ringbuffer.Test {
    final RingBuffer<Event> ringBuffer;

    RingBufferTest(RingBufferBuilder<Event> builder) {
        ringBuffer = builder.build();
    }

    @Test
    void testClass() {
        assertEquals(getClazz(), ringBuffer.getClass());
    }

    abstract Class<?> getClazz();

    @Test
    void testWriteAndRead() {
        runTest(getSum(), getBenchmarkRepeatTimes());
    }

    abstract int getBenchmarkRepeatTimes();

    abstract long getSum();
}
