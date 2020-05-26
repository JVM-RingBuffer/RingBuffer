package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ManyReadersBlockingTest extends ManyReadersBlockingContentionTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyReadersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
