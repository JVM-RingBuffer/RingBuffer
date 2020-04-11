package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyWritersTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(MANY_READERS_OR_WRITERS_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    public long run() {
        PrefilledKeyedWriter.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
