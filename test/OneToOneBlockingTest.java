package eu.menzani.ringbuffer;

class OneToOneBlockingTest extends RingBufferTest {
    OneToOneBlockingTest() {
        super(RingBuffer.<Event>empty(BLOCKING_SIZE)
                .oneReader()
                .oneWriter()
                .blocking()
                .withGC());
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
        Writer writer = Writer.newWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
