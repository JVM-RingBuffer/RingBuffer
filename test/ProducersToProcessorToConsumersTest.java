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
    public void testConcurrency() throws InterruptedException {
        assertEquals(2999997000000L, run());
    }

    private long run() throws InterruptedException {
        TestThreadGroup readerGroup = Reader.newGroup(processorToConsumers);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(producersToProcessor);
        Processor processor = new Processor(RingBufferTest.TOTAL_ELEMENTS);
        readerGroup.reportPerformance();
        writerGroup.reportPerformance();
        processor.reportPerformance();
        return readerGroup.getReaderSum();
    }

    private class Processor extends TestThread {
        private Processor(int numIterations) {
            super(numIterations, null);
        }

        @Override
        void tick(int i) {
            processorToConsumers.put(producersToProcessor.take());
        }
    }
}
