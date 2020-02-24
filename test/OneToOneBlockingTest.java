package eu.menzani.ringbuffer;

public class OneToOneBlockingTest extends RingBufferTest {
    public OneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .oneWriter()
                .blocking()
                .withGC());
    }

    int run() throws InterruptedException {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        new Writer(NUM_ITERATIONS, ringBuffer);
        reader.join();
        return reader.getSum();
    }
}
