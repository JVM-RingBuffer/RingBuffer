package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;

public class LocalRingBufferTest extends RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new LocalRingBufferTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 50;
    }

    @Override
    long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    long testSum() {
        Writer.runSync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runSync(NUM_ITERATIONS, RING_BUFFER);
    }
}
