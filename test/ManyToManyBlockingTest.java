package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyToManyBlockingTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyToManyBlockingTest().run();
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
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
