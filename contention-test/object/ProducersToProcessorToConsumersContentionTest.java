package test.object;

import org.ringbuffer.object.EmptyRingBuffer;
import org.ringbuffer.object.PrefilledOverwritingRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.wait.YieldBusyWaitStrategy;
import test.Profiler;

public class ProducersToProcessorToConsumersContentionTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> PRODUCERS_RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyWriters()
                    .oneReader()
                    .blocking()
                    .withGC()
                    .build();
    public static final PrefilledOverwritingRingBuffer<Event> CONSUMERS_RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneWriter()
                    .manyReaders()
                    .waitingWith(YieldBusyWaitStrategy.getDefault())
                    .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, TOTAL_ELEMENTS);
        Writer.startGroupAsync(PRODUCERS_RING_BUFFER, profiler);
        Processor.startAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER, profiler);
    }
}
