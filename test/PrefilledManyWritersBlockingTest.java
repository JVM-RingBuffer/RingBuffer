package eu.menzani.ringbuffer;

class PrefilledManyWritersBlockingTest extends RingBufferTest {
    PrefilledManyWritersBlockingTest() {
        super(RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                .oneReader()
                .manyWriters()
                .blocking());
    }

    @Override
    Class<?> getClazz() {
        return VolatileBlockingOrDiscardingRingBuffer.class;
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
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
