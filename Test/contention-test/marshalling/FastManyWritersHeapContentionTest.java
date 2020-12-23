package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class FastManyWritersHeapContentionTest extends RingBufferTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastManyWritersHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastHeapWriter.startGroupAsync(RING_BUFFER, profiler);
        return FastHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
