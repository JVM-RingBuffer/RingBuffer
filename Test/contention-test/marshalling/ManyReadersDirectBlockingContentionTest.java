package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyReadersDirectBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final DirectRingBuffer RING_BUFFER =
                DirectRingBuffer.withCapacity(BLOCKING_SIZE)
                        .manyReaders()
                        .oneWriter()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyReadersDirectBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectWriter.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedDirectReader.runGroupAsync(getRingBuffer(), profiler);
    }

    DirectRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
