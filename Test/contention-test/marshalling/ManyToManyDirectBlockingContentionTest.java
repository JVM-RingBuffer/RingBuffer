package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyToManyDirectBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final DirectRingBuffer RING_BUFFER =
                DirectRingBuffer.withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyToManyDirectBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedDirectReader.runGroupAsync(getRingBuffer(), profiler);
    }

    DirectRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
