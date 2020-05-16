package test;

import eu.menzani.ringbuffer.OverwritingPrefilledRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyWritersTest extends RingBufferTest {
    public static final OverwritingPrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersTest().run();
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
        OverwritingPrefilledWriter.startGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
