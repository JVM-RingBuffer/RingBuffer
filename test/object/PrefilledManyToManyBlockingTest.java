package test.object;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyToManyBlockingTest extends PrefilledManyToManyBlockingContentionTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
