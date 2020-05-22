package test;

import eu.menzani.ringbuffer.object.PrefilledRingBuffer;

public class PrefilledManyReadersBlockingTest extends RingBufferTest {
    public static final PrefilledRingBuffer<Event> RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .fillWith(FILLER)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersBlockingTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
    }

    @Override
    long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    long testSum() {
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
