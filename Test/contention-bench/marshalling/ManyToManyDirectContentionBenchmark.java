package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyToManyDirectContentionBenchmark extends RingBufferBenchmark {
    public static final DirectClearingRingBuffer RING_BUFFER =
            DirectRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyToManyDirectContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.startGroupAsync(RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
