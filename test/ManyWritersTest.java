package eu.menzani.ringbuffer;

public class ManyWritersTest extends RingBufferTest {
    public ManyWritersTest() {
        super(AtomicWriteRingBuffer.class, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .oneReader()
                .manyWriters());
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
