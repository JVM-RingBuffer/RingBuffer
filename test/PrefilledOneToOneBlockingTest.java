package eu.menzani.ringbuffer;

public class PrefilledOneToOneBlockingTest extends RingBufferTest {
    public PrefilledOneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 499999500000L, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event::new)
                .oneReader()
                .oneWriter()
                .blocking());
    }

    long run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.join();
        return reader.getSum();
    }
}
