package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class LockfreeManyReadersContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                        .manyReaders()
                        .oneWriter()
                        .lockfree()
                        .build();
    }

    public static void main(String[] args) {
        new LockfreeManyReadersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
