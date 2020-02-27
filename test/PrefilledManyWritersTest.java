package eu.menzani.ringbuffer;

public class PrefilledManyWritersTest extends RingBufferTest {
    public PrefilledManyWritersTest() {
        super(VolatileRingBuffer.class, 2999997000000L, RingBuffer.prefilled(TOTAL_ELEMENTS + 1, Event.RING_BUFFER_FILLER)
                .oneReader()
                .manyWriters());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 10;
    }

    long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
