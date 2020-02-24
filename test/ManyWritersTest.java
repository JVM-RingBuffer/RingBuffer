package eu.menzani.ringbuffer;

public class ManyWritersTest extends RingBufferTest {
    public ManyWritersTest() {
        super(AtomicWriteRingBuffer.class, 2999997000000L, RingBuffer.<Event>empty(TOTAL_ELEMENTS + 1)
                .oneReader()
                .manyWriters());
    }

    long run() throws InterruptedException {
        Reader reader = new Reader(TOTAL_ELEMENTS, ringBuffer);
        for (int i = 0; i < CONCURRENCY; i++) {
            new Writer(NUM_ITERATIONS, ringBuffer);
        }
        reader.join();
        return reader.getSum();
    }
}
