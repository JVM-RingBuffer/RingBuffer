package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class ManyReadersHeapBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final HeapRingBuffer RING_BUFFER =
                HeapRingBuffer.withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyReadersHeapBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapWriter.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedHeapReader.runGroupAsync(getRingBuffer(), profiler);
    }

    HeapRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
