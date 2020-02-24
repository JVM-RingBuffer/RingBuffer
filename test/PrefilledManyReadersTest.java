package eu.menzani.ringbuffer;

public class PrefilledManyReadersTest extends RingBufferTest {
    public PrefilledManyReadersTest() {
        super(AtomicReadRingBuffer.class, 17999997000000L, RingBuffer.prefilled(TOTAL_ELEMENTS + 1, Event::new)
                .manyReaders()
                .oneWriter());
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
