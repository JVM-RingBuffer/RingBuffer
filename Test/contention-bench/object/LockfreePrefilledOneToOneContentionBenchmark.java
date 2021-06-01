package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreePrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;

public class LockfreePrefilledOneToOneContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreePrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(LOCKFREE_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreePrefilledOneToOneContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreePrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreePrefilledReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
