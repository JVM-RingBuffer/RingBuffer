package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreeRingBuffer;
import org.ringbuffer.object.RingBuffer;

public class LockfreeManyToManyContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final LockfreeRingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                        .manyReaders()
                        .manyWriters()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeManyToManyContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.startGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
