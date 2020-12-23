package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class FastManyReadersDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastManyReadersDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return FastDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
