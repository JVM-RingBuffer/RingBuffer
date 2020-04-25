package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyToManyBlockingPerfTest extends PrefilledManyToManyBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER = RingBuffer.prefilled(NOT_ONE_TO_ONE_SIZE, FILLER)
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
