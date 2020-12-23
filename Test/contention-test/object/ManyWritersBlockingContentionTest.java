package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class ManyWritersBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyWritersBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
    }

    RingBuffer<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
