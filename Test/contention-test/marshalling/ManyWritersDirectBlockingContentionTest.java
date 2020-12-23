package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

public class ManyWritersDirectBlockingContentionTest extends RingBufferTest {
    public static class Holder {
        public static final DirectRingBuffer RING_BUFFER =
                DirectRingBuffer.withCapacity(BLOCKING_SIZE)
                        .oneReader()
                        .manyWriters()
                        .blocking()
                        .build();
    }

    public static void main(String[] args) {
        new ManyWritersDirectBlockingContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.startGroupAsync(getRingBuffer(), profiler);
        return DirectReader.runAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
    }

    DirectRingBuffer getRingBuffer() {
        return Holder.RING_BUFFER;
    }
}
