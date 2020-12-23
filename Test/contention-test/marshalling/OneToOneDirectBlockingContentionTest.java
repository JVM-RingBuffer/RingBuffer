package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class OneToOneDirectBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final DirectRingBuffer RING_BUFFER =
                DirectRingBuffer.withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new OneToOneDirectBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectWriter.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return DirectReader.runAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
    }

    DirectRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
