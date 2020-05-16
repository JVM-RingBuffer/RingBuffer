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
        new ProducersToProcessorToConsumersPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runGroupAsync(PRODUCERS_RING_BUFFER);
        Processor.runAsync();
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
