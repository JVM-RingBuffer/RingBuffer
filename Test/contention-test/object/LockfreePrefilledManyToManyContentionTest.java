package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class LockfreePrefilledManyToManyContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreePrefilledManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
