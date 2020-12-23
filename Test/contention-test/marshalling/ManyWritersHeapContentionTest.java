package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyWritersHeapContentionTest extends RingBufferTest {
    public static final HeapClearingRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapClearingWriter.startGroupAsync(RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
