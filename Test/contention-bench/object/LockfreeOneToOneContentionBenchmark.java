package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreeRingBuffer;
import org.ringbuffer.object.RingBuffer;

public class LockfreeOneToOneContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final LockfreeRingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_ONE_TO_ONE_SIZE)
                        .oneReader()
                        .oneWriter()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeOneToOneContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeWriter.startAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
