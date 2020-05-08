package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyReadersTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 34;
    }

    @Override
    long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    long testSum() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
