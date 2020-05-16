package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyWritersBlockingTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyWritersBlockingTest().run();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
