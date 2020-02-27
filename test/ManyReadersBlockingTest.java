package eu.menzani.ringbuffer;

public class ManyReadersBlockingTest extends RingBufferTest {
    public ManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, 17999997000000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .manyReaders()
                .oneWriter()
                .blocking()
                .withGC());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 5;
    }

    long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        Writer writer = Writer.newWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
