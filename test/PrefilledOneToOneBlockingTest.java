package eu.menzani.ringbuffer;

public class PrefilledOneToOneBlockingTest extends RingBufferTest {
    public PrefilledOneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .oneReader()
                .oneWriter()
                .blocking());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS);
        new PrefilledWriter(NUM_ITERATIONS);
        reader.join();
        return reader.getSum();
    }
}
