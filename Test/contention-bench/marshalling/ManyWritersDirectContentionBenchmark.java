package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyWritersDirectContentionBenchmark extends RingBufferBenchmark {
    public static final DirectClearingRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersDirectContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.startGroupAsync(RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
