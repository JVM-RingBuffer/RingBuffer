package perftest;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyReadersBlockingTest implements RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(BLOCKING_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyReadersBlockingTest().runTest();
    }

    @Override
    public int getBenchmarkRepeatTimes() {
        return 10;
    }

    @Override
    public long getSum() {
        return MANY_READERS_SUM;
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
