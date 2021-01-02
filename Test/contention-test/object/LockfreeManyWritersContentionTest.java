package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class LockfreeManyWritersContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                        .oneReader()
                        .manyWriters()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
    }
}
