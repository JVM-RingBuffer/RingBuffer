package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyWritersBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final PrefilledRingBuffer2<Event> RING_BUFFER =
                PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .fillWith(FILLER)
                        .oneReader()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
