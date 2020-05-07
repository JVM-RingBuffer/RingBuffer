package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledLocalRingBufferTest implements RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(ONE_TO_ONE_SIZE, FILLER)
                    .build();

    public static void main(String[] args) {
        new PrefilledLocalRingBufferTest().runTest();
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
        PrefilledWriter.runSync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runSync(NUM_ITERATIONS, RING_BUFFER);
    }
}
