package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledOneToOneBlockingTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 50;
    }

    @Override
    public long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    public long run() {
        Reader reader = Reader.newReader(NUM_ITERATIONS, RING_BUFFER);
        PrefilledWriter writer = new PrefilledWriter(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
