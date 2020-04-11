package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyReadersTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                    .manyReaders()
                    .oneWriter()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyReadersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 34;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
