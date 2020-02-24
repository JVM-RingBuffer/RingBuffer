package eu.menzani.ringbuffer;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public PrefilledManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, 0L, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .manyReaders()
                .oneWriter()
                .blocking());
    }

    long run() throws InterruptedException {
        ReaderGroup readerGroup = new ReaderGroup();
        for (int i = 0; i < CONCURRENCY; i++) {
            readerGroup.add(new Reader(NUM_ITERATIONS, ringBuffer));
        }
        new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        return readerGroup.getSum();
    }
}
