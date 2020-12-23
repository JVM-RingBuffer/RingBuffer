package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class ManyToManyBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyToManyBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return Reader.runGroupAsync(getRingBuffer(), profiler);
    }

    RingBuffer<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
