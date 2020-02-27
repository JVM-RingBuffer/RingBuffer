package eu.menzani.ringbuffer;

public class ManyWritersBlockingTest extends RingBufferTest {
    public ManyWritersBlockingTest() {
        super(AtomicWriteBlockingOrDiscardingRingBuffer.class, 2999997000000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .manyWriters()
                .blocking()
                .withGC());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 5;
    }

    long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = Writer.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
