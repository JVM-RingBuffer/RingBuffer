package test.object;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyReadersBlockingTest extends PrefilledManyReadersBlockingContentionTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
