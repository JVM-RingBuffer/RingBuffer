package eu.menzani.ringbuffer;

public class OneToOneTest extends RingBufferTest {
    public OneToOneTest() {
        super(VolatileRingBuffer.class, 499999500000L, RingBuffer.<Event>empty(NUM_ITERATIONS + 1)
                .oneReader()
                .oneWriter());
    }

    long run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        new Writer(NUM_ITERATIONS, ringBuffer);
        reader.join();
        return reader.getSum();
    }
}
