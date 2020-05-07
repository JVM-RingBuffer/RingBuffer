package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyReadersBlockingTest implements RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyReadersBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = Reader.runGroupAsync(RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
