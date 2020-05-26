package test.object;

import eu.menzani.ringbuffer.object.EmptyRingBuffer;

public class ManyToManyBlockingTest extends ManyToManyBlockingContentionTest {
    public static final EmptyRingBuffer<Event> RING_BUFFER =
            EmptyRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .manyWriters()
                    .blocking()
                    .build();

    public static void main(String[] args) {
        new ManyToManyBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runGroupAsync(RING_BUFFER);
        return Reader.runGroupAsync(RING_BUFFER);
    }
}
