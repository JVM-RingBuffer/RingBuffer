package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;
import org.ringbuffer.marshalling.LockfreeDirectRingBuffer;

public class LockfreeManyWritersDirectContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreeDirectRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeManyWritersDirectContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.startGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
