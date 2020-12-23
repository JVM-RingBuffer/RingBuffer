package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class ManyReadersBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyReadersBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return Reader.runGroupAsync(getRingBuffer(), profiler);
    }

    RingBuffer<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
