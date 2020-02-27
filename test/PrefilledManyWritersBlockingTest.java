package eu.menzani.ringbuffer;

public class PrefilledManyWritersBlockingTest extends RingBufferTest {
    public PrefilledManyWritersBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 2999997000000L, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event.RING_BUFFER_FILLER)
                .oneReader()
                .manyWriters()
                .blocking());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 5;
    }

    long run() {
        Reader reader = Reader.newReader(TOTAL_ELEMENTS, ringBuffer);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.newGroup(ringBuffer);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
