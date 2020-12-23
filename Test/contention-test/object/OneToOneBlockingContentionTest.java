package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class OneToOneBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new OneToOneBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return Reader.runAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
    }

    RingBuffer<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
