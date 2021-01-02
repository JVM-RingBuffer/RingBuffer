package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class LockfreeManyWritersDirectContentionTest extends RingBufferTest {
    public static final DirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyWritersDirectContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.startGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
