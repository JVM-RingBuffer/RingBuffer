package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProducersToProcessorToConsumersTest implements eu.menzani.ringbuffer.Test {
    private RingBuffer<Event> producersToProcessor;
    private RingBuffer<Event> processorToConsumers;

    @Before
    public void setUp() {
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
    public void testClasses() {
        assertEquals(VolatileRingBuffer.class, producersToProcessor.getClass());
        assertEquals(AtomicReadBlockingOrDiscardingRingBuffer.class, processorToConsumers.getClass());
    }

    @Test
    public void testWritesAndReads() {
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

    private class Processor extends TestThread {
        Processor(int numIterations) {
            super(numIterations, null);
        }

        @Override
        void loop() {
            int numIterations = getNumIterations();
            RingBuffer<Event> processorToConsumers = ProducersToProcessorToConsumersTest.this.processorToConsumers;
            RingBuffer<Event> producersToProcessor = ProducersToProcessorToConsumersTest.this.producersToProcessor;
            for (int i = 0; i < numIterations; i++) {
                processorToConsumers.put(producersToProcessor.take());
            }
        }
    }
}
