package eu.menzani.ringbuffer;

class PrefilledOneToOneTest extends RingBufferTest {
    PrefilledOneToOneTest() {
        super(RingBuffer.prefilled(ONE_TO_ONE_SIZE, FILLER)
                .oneReader()
                .oneWriter());
    }

    @Override
    Class<?> getClazz() {
        return VolatileRingBuffer.class;
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
