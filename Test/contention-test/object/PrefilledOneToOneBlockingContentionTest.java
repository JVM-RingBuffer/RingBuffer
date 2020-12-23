package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledOneToOneBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final PrefilledRingBuffer2<Event> RING_BUFFER =
                PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .fillWith(FILLER)
                        .oneReader()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return Reader.runAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
