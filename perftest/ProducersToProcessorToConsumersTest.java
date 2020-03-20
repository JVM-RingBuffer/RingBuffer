package perftest;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;

public class ProducersToProcessorToConsumersTest implements RingBufferTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER = RingBuffer.<Event>empty(BLOCKING_SIZE)
            .manyWriters()
            .oneReader()
            .blocking()
            .withGC()
            .build();
    public static final RingBuffer<Event> CONSUMERS_RING_BUFFER = RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
            .oneWriter()
            .manyReaders()
            .waitingWith(YieldBusyWaitStrategy.getDefault())
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
        TestThreadGroup readerGroup = BatchReader.runGroupAsync(CONSUMERS_RING_BUFFER);
        TestThreadGroup writerGroup = Writer.runGroupAsync(PRODUCERS_RING_BUFFER);
        Processor processor = Processor.runAsync(TOTAL_ELEMENTS);
        readerGroup.reportPerformance();
        writerGroup.reportPerformance();
        processor.reportPerformance();
        return readerGroup.getReaderSum();
    }

    private static class Processor extends TestThread {
        static Processor runAsync(int numIterations) {
            Processor thread = new Processor(numIterations);
            thread.start();
            return thread;
        }

        Processor(int numIterations) {
            super(numIterations, null);
        }

        @Override
        void loop() {
            int numIterations = getNumIterations();
            RingBuffer<Event> consumersRingBuffer = CONSUMERS_RING_BUFFER;
            RingBuffer<Event> producersRingBuffer = PRODUCERS_RING_BUFFER;
            for (int i = 0; i < numIterations; i++) {
                consumersRingBuffer.put(producersRingBuffer.take());
            }
        }
    }
}
