package eu.menzani.ringbuffer;

public class OneToOneTest extends RingBufferTest {
    public OneToOneTest() {
        super(VolatileRingBuffer.class, 499999500000L, RingBuffer.<Event>empty(NUM_ITERATIONS + 1)
                .oneReader()
                .oneWriter());
    }

    long run() {
        Reader reader = new Reader(NUM_ITERATIONS, ringBuffer);
        Writer writer = new Writer(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
