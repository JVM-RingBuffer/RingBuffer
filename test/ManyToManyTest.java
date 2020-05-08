package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyToManyTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyToManyTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
    }

    @Override
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    long testSum() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
