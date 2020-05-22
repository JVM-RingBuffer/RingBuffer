package test;

import eu.menzani.ringbuffer.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyWritersTest extends RingBufferTest {
    public static final PrefilledOverwritingRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    long testSum() {
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
