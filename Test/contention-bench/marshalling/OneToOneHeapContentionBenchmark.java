package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class OneToOneHeapContentionBenchmark extends RingBufferBenchmark {
    public static final HeapClearingRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new OneToOneHeapContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapClearingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
