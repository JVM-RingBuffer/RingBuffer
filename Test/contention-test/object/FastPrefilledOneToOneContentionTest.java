package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class FastPrefilledOneToOneContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(FAST_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastPrefilledOneToOneContentionTest().runBenchmark();
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
