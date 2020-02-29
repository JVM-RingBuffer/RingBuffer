package eu.menzani.ringbuffer;

public class ManyReadersTest extends RingBufferTest {
    public ManyReadersTest() {
        super(RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
                .manyReaders()
                .oneWriter());
    }

    @Override
    Class<?> getClazz() {
        return AtomicReadRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 40;
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
