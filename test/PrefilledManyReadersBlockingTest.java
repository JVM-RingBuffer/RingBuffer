package eu.menzani.ringbuffer;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public PrefilledManyReadersBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 17999997000000L, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event.RING_BUFFER_FILLER)
                .manyReaders()
                .oneWriter()
                .blocking());
    }

    long run() throws InterruptedException {
        TestThreadGroup readerGroup = SynchronizedReader.newGroup(ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        readerGroup.reportPerformance();
        writer.reportPerformance();
        return readerGroup.getReaderSum();
    }
}