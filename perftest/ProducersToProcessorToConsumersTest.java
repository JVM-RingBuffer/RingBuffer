package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;

public class ProducersToProcessorToConsumersTest implements Test {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER = RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
            .manyWriters()
            .oneReader()
            .build();
    public static final RingBuffer<Event> CONSUMERS_RING_BUFFER = RingBuffer.<Event>empty(BLOCKING_SIZE)
            .oneWriter()
            .manyReaders()
            .blocking()
            .waitingWith(YieldBusyWaitStrategy.getDefault())
            .withGC()
            .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = Reader.newGroup(CONSUMERS_RING_BUFFER);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(PRODUCERS_RING_BUFFER);
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
            RingBuffer<Event> consumersRingBuffer = ProducersToProcessorToConsumersTest.CONSUMERS_RING_BUFFER;
            RingBuffer<Event> producersRingBuffer = ProducersToProcessorToConsumersTest.PRODUCERS_RING_BUFFER;
            for (int i = 0; i < numIterations; i++) {
                consumersRingBuffer.put(producersRingBuffer.take());
            }
        }
    }
}
