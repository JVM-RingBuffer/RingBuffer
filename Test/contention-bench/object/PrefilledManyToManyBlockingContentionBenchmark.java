package bench.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;

public class PrefilledManyToManyBlockingContentionBenchmark extends RingBufferBenchmark {
    public static class Holder {
        public static final PrefilledRingBuffer2<Event> RING_BUFFER =
                PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                        .fillWith(FILLER)
                        .manyReaders()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedReader.runGroupAsync(getRingBuffer(), profiler);
    }

    PrefilledRingBuffer2<Event> getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
