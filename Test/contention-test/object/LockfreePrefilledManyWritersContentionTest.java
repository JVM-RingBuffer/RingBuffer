package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class LockfreePrefilledManyWritersContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreePrefilledManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
