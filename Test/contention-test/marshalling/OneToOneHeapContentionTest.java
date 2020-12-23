package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class OneToOneHeapContentionTest extends RingBufferTest {
    public static final HeapClearingRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new OneToOneHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapClearingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
