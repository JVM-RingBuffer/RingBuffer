package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.RingBuffer;

public class ManyWritersBlockingContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final RingBuffer<Event> RING_BUFFER =
                RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyWritersBlockingContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
    }

    RingBuffer<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
