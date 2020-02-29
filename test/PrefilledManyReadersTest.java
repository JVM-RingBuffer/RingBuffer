package eu.menzani.ringbuffer;

public class PrefilledManyReadersTest extends RingBufferTest {
    public PrefilledManyReadersTest() {
        super(RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                .manyReaders()
                .oneWriter());
    }

    @Override
    Class<?> getClazz() {
        return AtomicReadRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 34;
    }

    @Override
    long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
