package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class OneToOneHeapBlockingContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final HeapRingBuffer RING_BUFFER =
                HeapRingBuffer.withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new OneToOneHeapBlockingContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapWriter.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return HeapReader.runAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
    }

    HeapRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
