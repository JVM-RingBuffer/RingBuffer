package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class FastManyReadersHeapContentionTest extends RingBufferTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastManyReadersHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastHeapWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return FastHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
