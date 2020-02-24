package eu.menzani.ringbuffer;

public class ManyReadersTest extends RingBufferTest {
    public ManyReadersTest() {
        super(AtomicReadRingBuffer.class, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .manyReaders()
                .oneWriter());
    }

    int run() throws InterruptedException {
        ReaderGroup readerGroup = new ReaderGroup();
        for (int i = 0; i < CONCURRENCY; i++) {
            readerGroup.add(new Reader(NUM_ITERATIONS, ringBuffer));
        }
        new Writer(TOTAL_ELEMENTS, ringBuffer);
        return readerGroup.getSum();
    }
}
