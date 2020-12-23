package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyWritersContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
