package test.object;

import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyWritersContentionTest extends RingBufferTest {
    public static final PrefilledOverwritingRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
