package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyReadersBlockingPerfTest extends PrefilledManyReadersBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(NOT_ONE_TO_ONE_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return SynchronizedReader.runGroupAsync(RING_BUFFER);
    }
}
