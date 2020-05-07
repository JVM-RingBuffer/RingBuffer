package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ProducersToProcessorToConsumersPerfTest extends ProducersToProcessorToConsumersTest {
    public static final EmptyRingBuffer<Event> PRODUCERS_RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyWriters()
                    .oneReader()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(PRODUCERS_RING_BUFFER);
        Processor.runAsync(TOTAL_ELEMENTS);
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
