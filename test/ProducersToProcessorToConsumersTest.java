package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProducersToProcessorToConsumersTest implements eu.menzani.ringbuffer.Test {
    private static RingBuffer<Event> producersToProcessor;
    private static RingBuffer<Event> processorToConsumers;

    @BeforeAll
    static void setUp() {
        producersToProcessor = RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                .manyWriters()
                .oneReader()
                .build();
        processorToConsumers = RingBuffer.<Event>empty(BLOCKING_SIZE)
                .oneWriter()
                .manyReaders()
                .blocking()
                .waitingWith(YieldBusyWaitStrategy.getDefault())
                .withGC()
                .build();
    }

    @Test
    void testClasses() {
        assertEquals(VolatileRingBuffer.class, producersToProcessor.getClass());
        assertEquals(AtomicReadBlockingOrDiscardingRingBuffer.class, processorToConsumers.getClass());
    }

    @Test
    void testWritesAndReads() {
        runTest(MANY_WRITERS_SUM, 10);
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = Reader.newGroup(processorToConsumers);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(producersToProcessor);
        Processor processor = new Processor(TOTAL_ELEMENTS);
        readerGroup.reportPerformance();
        writerGroup.reportPerformance();
        processor.reportPerformance();
        return readerGroup.getReaderSum();
    }

    private static class Processor extends TestThread {
        Processor(int numIterations) {
            super(numIterations, null);
        }

        @Override
        void loop() {
            int numIterations = getNumIterations();
            RingBuffer<Event> processorToConsumers = ProducersToProcessorToConsumersTest.processorToConsumers;
            RingBuffer<Event> producersToProcessor = ProducersToProcessorToConsumersTest.producersToProcessor;
            for (int i = 0; i < numIterations; i++) {
                processorToConsumers.put(producersToProcessor.take());
            }
        }
    }
}
