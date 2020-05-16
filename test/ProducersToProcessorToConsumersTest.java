package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.OverwritingPrefilledRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;

public class ProducersToProcessorToConsumersTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> PRODUCERS_RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyWriters()
                    .oneReader()
                    .blocking()
                    .withGC()
                    .build();
    public static final OverwritingPrefilledRingBuffer<Event> CONSUMERS_RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
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
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    long testSum() {
        Writer.startGroupAsync(PRODUCERS_RING_BUFFER);
        Processor.startAsync();
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
