package eu.menzani.ringbuffer;

public class PrefilledManyWritersTest extends RingBufferTest {
    public PrefilledManyWritersTest() {
        super(RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                .oneReader()
                .manyWriters());
    }

    @Override
    Class<?> getClazz() {
        return VolatileRingBuffer.class;
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
