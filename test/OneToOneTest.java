package eu.menzani.ringbuffer;

public class OneToOneTest extends RingBufferTest {
    public OneToOneTest() {
        super(RingBuffer.<Event>empty(ONE_TO_ONE_SIZE)
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
        Writer writer = Writer.newWriter(NUM_ITERATIONS, ringBuffer);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
