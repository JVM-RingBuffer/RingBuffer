package eu.menzani.ringbuffer;

public class PrefilledOneToOneTest extends RingBufferTest {
    public PrefilledOneToOneTest() {
        super(VolatileRingBuffer.class, RingBuffer.prefilled(NUM_ITERATIONS + 1, Event::new)
                .oneReader()
                .oneWriter());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS);
        new PrefilledWriter(NUM_ITERATIONS);
        reader.join();
        return reader.getSum();
    }
}
