package eu.menzani.ringbuffer;

public class OneToOneBlockingTest extends RingBufferTest {
    public OneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 499999500000L, RingBuffer.<Event>empty(SMALL_BUFFER_SIZE)
                .oneReader()
                .oneWriter()
                .blocking()
                .withGC());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 80;
    }

    long run() {
        Reader reader = Reader.newReader(NUM_ITERATIONS, ringBuffer);
        Writer writer = Writer.newWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
