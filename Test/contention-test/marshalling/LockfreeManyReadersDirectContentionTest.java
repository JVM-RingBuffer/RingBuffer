package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class LockfreeManyReadersDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyReadersDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
