package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class OneToOneBlockingTest implements RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new OneToOneBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 50;
    }

    @Override
    public long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    public long run() {
        Writer writer = Writer.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
