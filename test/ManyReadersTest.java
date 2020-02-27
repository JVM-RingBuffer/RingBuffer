package eu.menzani.ringbuffer;

public class ManyReadersTest extends RingBufferTest {
    public ManyReadersTest() {
        super(AtomicReadRingBuffer.class, 17999997000000L, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .manyReaders()
                .oneWriter());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 40;
    }

    long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        Writer writer = Writer.newWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
