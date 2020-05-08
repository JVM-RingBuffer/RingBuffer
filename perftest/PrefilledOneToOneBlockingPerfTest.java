package test;

import eu.menzani.ringbuffer.PrefilledRingBuffer;

public class PrefilledOneToOneBlockingPerfTest extends PrefilledOneToOneBlockingTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.withCapacityAndFiller(ONE_TO_ONE_SIZE, FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
