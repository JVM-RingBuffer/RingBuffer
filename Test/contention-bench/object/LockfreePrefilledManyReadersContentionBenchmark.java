package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreePrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;

public class LockfreePrefilledManyReadersContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreePrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreePrefilledManyReadersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreePrefilledReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
