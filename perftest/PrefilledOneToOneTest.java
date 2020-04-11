package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledOneToOneTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneTest().runTest();
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
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
