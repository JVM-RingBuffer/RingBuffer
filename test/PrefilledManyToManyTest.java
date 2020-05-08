package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyToManyTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
                    .manyReaders()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyToManyTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    long testSum() {
        TestThreadGroup group = PrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}