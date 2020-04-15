package test;

import eu.menzani.ringbuffer.RingBuffer;

public class OneToOneTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new OneToOneTest().runTest();
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
