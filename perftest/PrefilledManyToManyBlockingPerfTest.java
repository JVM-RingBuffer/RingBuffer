package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledManyToManyBlockingPerfTest extends PrefilledManyToManyBlockingTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(NOT_ONE_TO_ONE_SIZE, FILLER)
            .manyReaders()
            .manyWriters()
            .blocking()
            .build();

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        return SynchronizedReader.runGroupAsync(RING_BUFFER);
    }
}
