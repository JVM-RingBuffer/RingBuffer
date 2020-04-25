package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyToManyTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER = RingBuffer.prefilled(NOT_ONE_TO_ONE_SIZE, FILLER)
            .manyReaders()
            .manyWriters()
            .build();

    public static void main(String[] args) {
        new PrefilledManyToManyTest().runTest();
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
        long sum = Reader.runGroupAsync(RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
