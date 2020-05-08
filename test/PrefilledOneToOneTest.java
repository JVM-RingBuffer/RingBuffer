package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledOneToOneTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 50;
    }

    @Override
    long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    long testSum() {
        PrefilledWriter writer = PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
