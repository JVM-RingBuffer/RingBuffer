package eu.menzani.ringbuffer;

class PrefilledManyReadersBlockingTest extends RingBufferTest {
    PrefilledManyReadersBlockingTest() {
        super(RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                .manyReaders()
                .oneWriter()
                .blocking());
    }

    @Override
    Class<?> getClazz() {
        return VolatileBlockingOrDiscardingRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 12;
    }

    @Override
    long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = SynchronizedReader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
