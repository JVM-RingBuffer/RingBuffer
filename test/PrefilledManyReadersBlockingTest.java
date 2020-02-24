package eu.menzani.ringbuffer;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public PrefilledManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .manyReaders()
                .oneWriter()
                .blocking());
    }

    int run() throws InterruptedException {
        ReaderGroup readerGroup = new ReaderGroup();
        for (int i = 0; i < CONCURRENCY; i++) {
            readerGroup.add(new Reader(NUM_ITERATIONS, ringBuffer));
        }
        new PrefilledWriter(TOTAL_ELEMENTS, ringBuffer);
        return readerGroup.getSum();
    }
}
