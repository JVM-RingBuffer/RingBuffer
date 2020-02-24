package eu.menzani.ringbuffer;

public class PrefilledManyWritersTest extends RingBufferTest {
    public PrefilledManyWritersTest() {
        super(VolatileRingBuffer.class, RingBuffer.prefilled(TOTAL_ELEMENTS + 1, Event::new)
                .oneReader()
                .manyWriters());
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
