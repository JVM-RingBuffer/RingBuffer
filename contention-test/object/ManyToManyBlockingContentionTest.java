package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ManyToManyBlockingContentionTest extends RingBufferTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .withGC()
                    .build();

    public static void main(String[] args) {
        new ManyToManyBlockingContentionTest().runBenchmark();
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
        Writer.startGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
