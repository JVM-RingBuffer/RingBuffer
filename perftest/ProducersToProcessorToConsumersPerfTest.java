package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ProducersToProcessorToConsumersPerfTest extends ProducersToProcessorToConsumersTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER = RingBuffer.<Event>empty(NOT_ONE_TO_ONE_SIZE)
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
        Processor.runAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER, CONSUMERS_RING_BUFFER);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
