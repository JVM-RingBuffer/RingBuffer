package test;

import eu.menzani.ringbuffer.RingBuffer;

public class LocalRingBufferTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(ONE_TO_ONE_SIZE)
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new LocalRingBufferTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 50;
    }

    @Override
    public long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    public long run() {
        Writer.runSync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runSync(NUM_ITERATIONS, RING_BUFFER);
    }
}
