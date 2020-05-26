package test.object;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyToManyBlockingContentionTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyToManyBlockingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        PrefilledWriter.startGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
