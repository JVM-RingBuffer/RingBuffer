package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyReadersHeapContentionBenchmark extends RingBufferBenchmark {
    public static final HeapClearingRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersHeapContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapClearingWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedHeapClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
