package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyReadersTest implements Test {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new ManyReadersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 40;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = Reader.newGroup(RING_BUFFER);
        Writer writer = Writer.newWriter(TOTAL_ELEMENTS, RING_BUFFER);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
