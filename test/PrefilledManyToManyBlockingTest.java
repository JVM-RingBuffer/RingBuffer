package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyToManyBlockingTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER = RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
            .manyReaders()
            .manyWriters()
            .blocking()
            .build();

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingTest().runTest();
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
        TestThreadGroup group = PrefilledSynchronizedWriter.startGroupAsync(RING_BUFFER);
        long sum = SynchronizedReader.runGroupAsync(RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
