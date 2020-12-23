package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyToManyHeapBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final HeapRingBuffer RING_BUFFER =
                HeapRingBuffer.withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyToManyHeapBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapWriter.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedHeapReader.runGroupAsync(getRingBuffer(), profiler);
    }

    HeapRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
