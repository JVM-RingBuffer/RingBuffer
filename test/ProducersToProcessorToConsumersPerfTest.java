package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ProducersToProcessorToConsumersPerfTest extends ProducersToProcessorToConsumersTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER = RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
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
        return BatchReader.runGroupAsync(READ_BUFFER_SIZE, CONSUMERS_RING_BUFFER);
    }
}
