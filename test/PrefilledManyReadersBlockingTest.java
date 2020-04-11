package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyReadersBlockingTest implements RingBufferTest {
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
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = SynchronizedReader.runGroupAsync(RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
