package eu.menzani.ringbuffer;

public class ManyReadersBlockingTest extends RingBufferTest {
    public ManyReadersBlockingTest() {
        super(AtomicReadBlockingOrDiscardingRingBuffer.class, 17999997000000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .manyReaders()
                .oneWriter()
                .blocking()
                .withGC());
    }

    long run() throws InterruptedException {
        ReaderGroup readerGroup = new ReaderGroup();
        for (int i = 0; i < CONCURRENCY; i++) {
            readerGroup.add(new Reader(NUM_ITERATIONS, ringBuffer));
        }
        new Writer(TOTAL_ELEMENTS, ringBuffer);
        return readerGroup.getSum();
    }
}
