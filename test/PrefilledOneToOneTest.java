package eu.menzani.ringbuffer;

public class PrefilledOneToOneTest extends RingBufferTest {
    public PrefilledOneToOneTest() {
        super(VolatileRingBuffer.class, 499999500000L, RingBuffer.prefilled(NUM_ITERATIONS + 1, Event::new)
                .oneReader()
                .oneWriter());
    }

    long run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.join();
        return reader.getSum();
    }
}
