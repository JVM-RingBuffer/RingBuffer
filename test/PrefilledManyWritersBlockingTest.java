package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyWritersBlockingTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingTest().runTest();
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
        long sum = Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
