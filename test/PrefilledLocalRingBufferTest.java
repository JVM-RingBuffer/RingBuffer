package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledLocalRingBufferTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(ONE_TO_ONE_SIZE, FILLER)
                    .build();

    public static void main(String[] args) {
        new PrefilledLocalRingBufferTest().run();
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
        PrefilledWriter.runSync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runSync(NUM_ITERATIONS, RING_BUFFER);
    }
}
