package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyWritersTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersTest().run();
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
        long sum = Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
