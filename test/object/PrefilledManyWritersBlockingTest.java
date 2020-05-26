package test.object;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyWritersBlockingTest extends PrefilledManyWritersBlockingContentionTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
