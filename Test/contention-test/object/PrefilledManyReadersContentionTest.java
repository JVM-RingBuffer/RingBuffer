package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyReadersContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
