package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyReadersTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 40;
    }

    @Override
    long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    long testSum() {
        Writer writer = Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
