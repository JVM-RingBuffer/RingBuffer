package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProducersToProcessorToConsumersTest {
    private RingBuffer<Event> producersToProcessor;
    private RingBuffer<Event> processorToConsumers;

    @Before
    public void setUp() {
        producersToProcessor = RingBuffer.prefilled(RingBufferTest.TOTAL_ELEMENTS + 1, Event.RING_BUFFER_FILLER)
                .manyWriters()
                .oneReader()
                .build();
        processorToConsumers = RingBuffer.<Event>empty(RingBufferTest.SMALL_BUFFER_SIZE)
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
        RingBufferTest.runTest(2999997000000L, this::run, 5);
    }

    private long run() {
        TestThreadGroup readerGroup = Reader.newGroup(processorToConsumers);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(producersToProcessor);
        Processor processor = new Processor(RingBufferTest.TOTAL_ELEMENTS);
        readerGroup.reportPerformance();
        writerGroup.reportPerformance();
        processor.reportPerformance();
        return readerGroup.getReaderSum();
    }

    private class Processor extends TestThread {
        Processor(int numIterations) {
            super(true, numIterations, null);
        }

        @Override
        void loop() {
            for (int i = 0; i < numIterations; i++) {
                processorToConsumers.put(producersToProcessor.take());
            }
        }
    }
}
