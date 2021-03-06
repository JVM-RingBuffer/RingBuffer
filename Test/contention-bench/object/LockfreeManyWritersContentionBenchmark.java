package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.LockfreeRingBuffer;
import org.ringbuffer.object.RingBuffer;

public class LockfreeManyWritersContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final LockfreeRingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                        .oneReader()
                        .manyWriters()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeManyWritersContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.startGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
    }
}
