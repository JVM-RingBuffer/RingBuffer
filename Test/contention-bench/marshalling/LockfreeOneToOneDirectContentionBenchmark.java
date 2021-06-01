package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;
import org.ringbuffer.marshalling.LockfreeDirectRingBuffer;

public class LockfreeOneToOneDirectContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreeDirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeOneToOneDirectContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeDirectWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
