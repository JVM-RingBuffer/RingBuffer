package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;

public class ManyWritersBlockingPerfTest extends ManyWritersBlockingTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyWritersBlockingPerfTest().run();
    }

    @Override
    long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
    }
}
