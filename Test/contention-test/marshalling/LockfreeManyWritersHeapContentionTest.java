package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class LockfreeManyWritersHeapContentionTest extends RingBufferTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyWritersHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.startGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
