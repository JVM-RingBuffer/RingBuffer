package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyWritersBlockingTest implements Test {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(BLOCKING_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyWritersBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, RING_BUFFER);
        TestThreadGroup writerGroup = Writer.newGroup(RING_BUFFER);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
