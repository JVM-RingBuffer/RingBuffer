package test.object;

import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledOneToOneContentionTest extends RingBufferTest {
    public static final PrefilledOverwritingRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneReader()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledOneToOneContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 50;
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        PrefilledOverwritingWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER);
    }
}
