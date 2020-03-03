package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class OneToOneTest implements Test {
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
        Reader reader = Reader.newReader(NUM_ITERATIONS, RING_BUFFER);
        Writer writer = Writer.newWriter(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
