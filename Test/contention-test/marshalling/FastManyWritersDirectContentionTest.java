package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class FastManyWritersDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastManyWritersDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.startGroupAsync(RING_BUFFER, profiler);
        return FastDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
