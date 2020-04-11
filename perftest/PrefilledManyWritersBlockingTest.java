package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class PrefilledManyWritersBlockingTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.prefilled(BLOCKING_SIZE, FILLER)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new PrefilledManyWritersBlockingTest().runTest();
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
        return AdvancingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
