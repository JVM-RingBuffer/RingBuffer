package eu.menzani.ringbuffer;

public class PrefilledManyReadersTest extends RingBufferTest {
    public PrefilledManyReadersTest() {
        super(AtomicReadRingBuffer.class, 17999997000000L, RingBuffer.prefilled(TOTAL_ELEMENTS + 1, Event.RING_BUFFER_FILLER)
                .manyReaders()
                .oneWriter());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 34;
    }

    long run() {
        TestThreadGroup readerGroup = Reader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}
