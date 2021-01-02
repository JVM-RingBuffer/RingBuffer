package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class LockfreeManyToManyContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                        .manyReaders()
                        .manyWriters()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
