package eu.menzani.ringbuffer;

public class PrefilledOneToOneBlockingTest extends RingBufferTest {
    public PrefilledOneToOneBlockingTest() {
        super(VolatileBlockingOrDiscardingRingBuffer.class, 499999500000L, RingBuffer.prefilled(SMALL_BUFFER_SIZE, Event.RING_BUFFER_FILLER)
                .oneReader()
                .oneWriter()
                .blocking());
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 100;
    }

    long run() {
        Reader reader = Reader.newReader(NUM_ITERATIONS, ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
