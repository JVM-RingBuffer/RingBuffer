package eu.menzani.ringbuffer;

class PrefilledOneToOneBlockingTest extends RingBufferTest {
    PrefilledOneToOneBlockingTest() {
        super(RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                .oneReader()
                .oneWriter()
                .blocking());
    }

    @Override
    Class<?> getClazz() {
        return VolatileBlockingOrDiscardingRingBuffer.class;
    }

    @Override
    int getBenchmarkRepeatTimes() {
        return 50;
    }

    @Override
    long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    public long run() {
        Reader reader = Reader.newReader(NUM_ITERATIONS, ringBuffer);
        PrefilledWriter writer = new PrefilledWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
