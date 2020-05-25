package test.object;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledOneToOneBlockingPerfTest extends PrefilledOneToOneBlockingTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
