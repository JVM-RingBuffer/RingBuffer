package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyWritersBlockingPerfTest extends PrefilledManyWritersBlockingTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
