package eu.menzani.ringbuffer;

import org.junit.Test;

import static org.junit.Assert.*;

abstract class RingBufferTest {
    static final int SMALL_BUFFER_SIZE = 5;

    static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 6;
    static final int TOTAL_ELEMENTS = CONCURRENCY * NUM_ITERATIONS;

    private final Class<? extends RingBuffer<?>> clazz;
    final RingBuffer<Event> ringBuffer;

    <T extends RingBuffer<?>> RingBufferTest(Class<T> clazz, RingBufferBuilder<Event> builder) {
        this.clazz = clazz;
        ringBuffer = builder.build();
    }

    @Test
    public void testClass() {
        assertEquals(clazz, ringBuffer.getClass());
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        assertEquals(run(), run());
    }

    abstract int run() throws InterruptedException;
}
