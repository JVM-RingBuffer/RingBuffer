package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledOneToOneContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
