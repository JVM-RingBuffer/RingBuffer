package eu.menzani.ringbuffer;

public class PrefilledManyWritersBlockingTest extends RingBufferTest {
    public PrefilledManyWritersBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .oneReader()
                .manyWriters()
                .blocking());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(TOTAL_ELEMENTS, ringBuffer);
        for (int i = 0; i < CONCURRENCY; i++) {
            new PrefilledSynchronizedWriter(NUM_ITERATIONS, ringBuffer);
        }
        reader.join();
        return reader.getSum();
    }
}
