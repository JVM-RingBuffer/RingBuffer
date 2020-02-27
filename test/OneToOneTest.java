package eu.menzani.ringbuffer;

public class OneToOneTest extends RingBufferTest {
    public OneToOneTest() {
        super(VolatileRingBuffer.class, 499999500000L, RingBuffer.<Event>empty(NUM_ITERATIONS + 1)
                .oneReader()
                .oneWriter());
    }

    long run() {
        Reader reader = Reader.newReader(NUM_ITERATIONS, ringBuffer);
        Writer writer = Writer.newWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
