package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyWritersTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 12;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
