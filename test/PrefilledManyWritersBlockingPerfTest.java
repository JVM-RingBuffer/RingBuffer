package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyWritersBlockingPerfTest extends PrefilledManyWritersBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledKeyedWriter.runGroupAsync(RING_BUFFER);
        return AdvancingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
