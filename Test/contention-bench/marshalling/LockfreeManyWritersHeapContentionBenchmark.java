package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import org.ringbuffer.marshalling.LockfreeHeapRingBuffer;

public class LockfreeManyWritersHeapContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreeHeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyWritersHeapContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.startGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
