package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class FastManyToManyDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastManyToManyDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.startGroupAsync(RING_BUFFER, profiler);
        return FastDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
