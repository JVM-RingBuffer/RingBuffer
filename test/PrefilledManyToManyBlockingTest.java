package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyToManyBlockingTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(BLOCKING_SIZE, FILLER)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingTest().run();
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
