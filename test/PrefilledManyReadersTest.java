package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyReadersTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 34;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
