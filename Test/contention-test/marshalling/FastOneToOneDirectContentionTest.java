package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class FastOneToOneDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastOneToOneDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        FastDirectWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return FastDirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
