package test;

import eu.menzani.ringbuffer.RingBuffer;

public class ManyWritersBlockingPerfTest extends ManyWritersBlockingTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>empty(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersBlockingPerfTest().runTest();
    }

    @Override
    public long run() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
