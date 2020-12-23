package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyReadersBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final PrefilledRingBuffer2<Event> RING_BUFFER =
                PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .fillWith(FILLER)
                        .manyReaders()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedReader.runGroupAsync(getRingBuffer(), profiler);
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
