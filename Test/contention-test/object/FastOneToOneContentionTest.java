package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class FastOneToOneContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(FAST_ONE_TO_ONE_SIZE)
                        .oneReader()
                        .oneWriter()
                        .withoutLocks()
                        .build();
    }

    public static void main(String[] args) {
        new FastOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
