package eu.menzani.ringbuffer;

public class ManyWritersBlockingTest extends RingBufferTest {
    public ManyWritersBlockingTest() {
        super(AtomicWriteBlockingOrDiscardingRingBuffer.class, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .manyWriters()
                .blocking()
                .withGC());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(TOTAL_ELEMENTS);
        for (int i = 0; i < CONCURRENCY; i++) {
            new Writer(NUM_ITERATIONS);
        }
        reader.join();
        return reader.getSum();
    }
}
