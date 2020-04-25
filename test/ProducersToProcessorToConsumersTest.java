package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.wait.YieldBusyWaitStrategy;

public class ProducersToProcessorToConsumersTest implements RingBufferTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER = RingBuffer.<Event>empty(BLOCKING_SIZE)
            .manyWriters()
            .oneReader()
            .blocking()
            .withGC()
            .build();
    public static final RingBuffer<Event> CONSUMERS_RING_BUFFER = RingBuffer.prefilled(NOT_ONE_TO_ONE_SIZE, FILLER)
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
        TestThreadGroup group = Writer.startGroupAsync(PRODUCERS_RING_BUFFER);
        Processor processor = Processor.startAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER, CONSUMERS_RING_BUFFER);
        long sum = SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER);
        processor.reportPerformance();
        group.reportPerformance();
        return sum;
    }
}
