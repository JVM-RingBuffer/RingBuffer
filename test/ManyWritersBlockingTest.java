package eu.menzani.ringbuffer;

public class ManyWritersBlockingTest extends RingBufferTest {
    public ManyWritersBlockingTest() {
        super(AtomicWriteBlockingOrDiscardingRingBuffer.class, 2999997000000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .manyWriters()
                .blocking()
                .withGC());
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
