package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyReadersBlockingPerfTest extends ManyReadersBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(MANY_READERS_OR_WRITERS_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
