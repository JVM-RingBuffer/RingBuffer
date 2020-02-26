package eu.menzani.ringbuffer;

public class OneToOneBlockingTest extends RingBufferTest {
    public OneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 499999500000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .oneWriter()
                .blocking()
                .withGC());
    }

    long run() {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        Writer writer = new Writer(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
