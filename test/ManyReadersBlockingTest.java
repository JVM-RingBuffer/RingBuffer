package eu.menzani.ringbuffer;

class ManyReadersBlockingTest extends RingBufferTest {
    ManyReadersBlockingTest() {
        super(RingBuffer.<Event>empty(BLOCKING_SIZE)
                .manyReaders()
                .oneWriter()
                .blocking()
                .withGC());
    }

    @Override
    Class<?> getClazz() {
        return AtomicReadBlockingOrDiscardingRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        Writer writer = Writer.newWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
