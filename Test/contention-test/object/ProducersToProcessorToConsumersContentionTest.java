package test.object;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.RingBuffer;

public class ProducersToProcessorToConsumersContentionTest extends RingBufferTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER =
            RingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                    .manyWriters()
                    .oneReader()
                    .lockfree()
                    .build();
    public static final PrefilledRingBuffer<Event> CONSUMERS_RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(LOCKFREE_NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneWriter()
                    .manyReaders()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(PRODUCERS_RING_BUFFER, profiler);
        Processor.startAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return Reader.runGroupAsync(CONSUMERS_RING_BUFFER, profiler);
    }
}
