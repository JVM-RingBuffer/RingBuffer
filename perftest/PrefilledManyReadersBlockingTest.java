package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyReadersBlockingTest implements Test {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 12;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = SynchronizedReader.newGroup(RING_BUFFER);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, RING_BUFFER);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
