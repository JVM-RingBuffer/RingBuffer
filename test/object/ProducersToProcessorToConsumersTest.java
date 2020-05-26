package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ProducersToProcessorToConsumersTest extends ProducersToProcessorToConsumersContentionTest {
    public static final EmptyRingBuffer<Event> PRODUCERS_RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyWriters()
                    .oneReader()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(PRODUCERS_RING_BUFFER);
        Processor.runAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
    }
}
