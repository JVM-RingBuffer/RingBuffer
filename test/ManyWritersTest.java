package eu.menzani.ringbuffer;

public class ManyWritersTest extends RingBufferTest {
    public ManyWritersTest() {
        super(AtomicWriteRingBuffer.class, 2999997000000L, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .oneReader()
                .manyWriters());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 12;
    }

    long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = Writer.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
