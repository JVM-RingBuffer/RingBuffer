package eu.menzani.ringbuffer;

public class OneToOneTest extends RingBufferTest {
    public OneToOneTest() {
        super(VolatileRingBuffer.class, RingBuffer.<Event>empty(NUM_ITERATIONS + 1)
                .oneReader()
                .oneWriter());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS);
        new Writer(NUM_ITERATIONS);
        reader.join();
        return reader.getSum();
    }
}
