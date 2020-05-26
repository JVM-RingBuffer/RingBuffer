package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ManyWritersContentionTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .build();

    public static void main(String[] args) {
        new ManyWritersContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Writer.startGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
