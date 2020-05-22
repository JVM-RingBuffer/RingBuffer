package test;

import eu.menzani.ringbuffer.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledOneToOneTest extends RingBufferTest {
    public static final PrefilledOverwritingRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneTest().runBenchmark();
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
        PrefilledOverwritingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
