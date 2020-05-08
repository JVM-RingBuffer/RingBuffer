package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(BLOCKING_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
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
