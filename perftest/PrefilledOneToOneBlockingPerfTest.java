package test;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledOneToOneBlockingPerfTest extends PrefilledOneToOneBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
