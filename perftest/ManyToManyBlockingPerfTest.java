package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyToManyBlockingPerfTest extends ManyToManyBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER = RingBuffer.<Event>empty(NOT_ONE_TO_ONE_SIZE)
            .manyReaders()
            .manyWriters()
            .blocking()
            .build();

    public static void main(String[] args) {
        new ManyToManyBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
