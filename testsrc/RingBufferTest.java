package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.system.ThreadBind;
import org.junit.Test;

import java.util.function.LongSupplier;

import static org.junit.Assert.*;

abstract class RingBufferTest {
    static final int SMALL_BUFFER_SIZE = 5;

    static final int NUM_ITERATIONS = 1_000_000;
    static final int CONCURRENCY = 6;
    static final int TOTAL_ELEMENTS = CONCURRENCY * NUM_ITERATIONS;

    static {
        ThreadBind.loadNativeLibrary();
    }

    private final Class<? extends RingBuffer<?>> clazz;
    private final long sum;
    final RingBuffer<Event> ringBuffer;

    <T extends RingBuffer<?>> RingBufferTest(Class<T> clazz, long sum, RingBufferBuilder<Event> builder) {
        this.clazz = clazz;
        this.sum = sum;
        ringBuffer = builder.build();
    }

    @Test
    public void testClass() {
        assertEquals(clazz, ringBuffer.getClass());
    }

    @Test
    public void testWriteAndRead() {
        runTest(sum, this::run, getBenchmarkRepeatTimes());
    }

    abstract int getBenchmarkRepeatTimes();

    abstract long run();

    static void runTest(long sum, LongSupplier test, int benchmarkRepeatTimes) {
        int repeatTimes = Benchmark.isEnabled() ? benchmarkRepeatTimes : 1;
        for (int i = 0; i < repeatTimes; i++) {
            assertEquals(sum, test.getAsLong());
        }
        Benchmark.report();
    }
}
