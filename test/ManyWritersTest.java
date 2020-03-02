package eu.menzani.ringbuffer;

class ManyWritersTest extends RingBufferTest {
    ManyWritersTest() {
        super(RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
                .oneReader()
                .manyWriters());
    }

    @Override
    Class<?> getClazz() {
        return AtomicWriteRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 12;
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
