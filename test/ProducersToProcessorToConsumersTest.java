package test;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledRingBuffer;
import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;

public class ProducersToProcessorToConsumersTest extends RingBufferTest {
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
        new ProducersToProcessorToConsumersTest().runBenchmark();
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
        Writer.startGroupAsync(PRODUCERS_RING_BUFFER);
        Processor.startAsync(PRODUCERS_RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
