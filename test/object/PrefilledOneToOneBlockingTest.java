package test.object;

import org.ringbuffer.object.PrefilledRingBuffer;
import test.Profiler;

public class PrefilledOneToOneBlockingTest extends PrefilledOneToOneBlockingContentionTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
