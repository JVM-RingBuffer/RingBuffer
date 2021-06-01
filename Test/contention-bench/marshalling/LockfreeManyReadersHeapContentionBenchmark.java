package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import org.ringbuffer.marshalling.LockfreeHeapRingBuffer;

public class LockfreeManyReadersHeapContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreeHeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyReadersHeapContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
