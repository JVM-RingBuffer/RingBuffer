package eu.menzani.ringbuffer;

public class ManyWritersBlockingTest extends RingBufferTest {
    public ManyWritersBlockingTest() {
        super(RingBuffer.<Event>empty(BLOCKING_SIZE)
                .oneReader()
                .manyWriters()
                .blocking()
                .withGC());
    }

    @Override
    Class<?> getClazz() {
        return AtomicWriteBlockingOrDiscardingRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = Writer.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
