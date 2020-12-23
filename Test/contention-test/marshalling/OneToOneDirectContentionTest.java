package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class OneToOneDirectContentionTest extends RingBufferTest {
    public static final DirectClearingRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new OneToOneDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectClearingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
